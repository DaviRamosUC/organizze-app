package com.devdavi.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devdavi.organizze.R;
import com.devdavi.organizze.adapter.AdapterMovimentacao;
import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityPrincipalBinding;
import com.devdavi.organizze.model.Movimentacao;
import com.devdavi.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private MaterialCalendarView calendarView;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapter;

    private String mesAnoSelecionado = "";
    List<Movimentacao> movimentacoes = new ArrayList<>();

    private final FirebaseAuth auth = ConfiguracaoFirebase.getAutenticacao();
    ;
    private final DatabaseReference idUsuario = ConfiguracaoFirebase.getIdUsuarioChild();
    private final DatabaseReference database = ConfiguracaoFirebase.getDatabase();
    ;
    private ValueEventListener evento;
    private ValueEventListener valueEventListenerMovimentacoes;

    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoTotal = 0.0;
    private final CharSequence[] meses = {
            "Janeiro", "Fevereiro", "Março",
            "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro",
            "Outubro", "Novembro", "Dezembro"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);

        calendarView = binding.contentPrincipal.calendarView;
        recyclerView = binding.contentPrincipal.recyclerMovimentos;
        swipe();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterMovimentacao(movimentacoes, getApplicationContext());
        recyclerView.setAdapter(adapter);

        binding.menuReceita.setOnClickListener(view -> {
            startActivity(new Intent(this, ReceitasActivity.class));
        });

        binding.menuDespesa.setOnClickListener(view -> {
            startActivity(new Intent(this, DespesasActivity.class));
        });


    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    private void excluirMovimentacao(RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Excluir Movimentação da Conta");
        dialog.setMessage("Você tem certeza que deseja realmente excluir esta movimentação da sua conta?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Sim", (dialogInterface, i) -> {
            int position = viewHolder.getAdapterPosition();
            Movimentacao movimentacao = movimentacoes.get(position);
            database.child("movimentacao")
                    .child(ConfiguracaoFirebase.getIdUsuarioCodificado())
                    .child(mesAnoSelecionado)
                    .child(movimentacao.getId())
                    .removeValue();
            adapter.notifyItemRemoved(position);
            atualizarSaldo(movimentacao);
        }).setNegativeButton("Não", (dialogInterface, i) -> {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG)
                    .show();
            adapter.notifyDataSetChanged();
        });
        dialog.create().show();


    }

    public void atualizarSaldo(Movimentacao movimentacao){
        if(movimentacao.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacao.getValor();
            idUsuario.child("receitaTotal").setValue(receitaTotal);
        }else{
            despesaTotal = despesaTotal - movimentacao.getValor();
            idUsuario.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void recuperaMovimentacoes() {
        String idUsuario = ConfiguracaoFirebase.getIdUsuarioCodificado();
        valueEventListenerMovimentacoes = database.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        movimentacoes.clear();
                        for (DataSnapshot dados : snapshot.getChildren()) {
                            Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                            if (movimentacao != null) {
                                movimentacao.setId(dados.getKey());
                                movimentacoes.add(movimentacao);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void recuperarResumo() {
        evento = idUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                if (usuario != null) {
                    despesaTotal = usuario.getDespesaTotal();
                    receitaTotal = usuario.getReceitaTotal();
                    resumoTotal = receitaTotal - despesaTotal;
                    DecimalFormat df = new DecimalFormat("0.00");
                    String resultadoFormatado = df.format(resumoTotal);
                    if (resultadoFormatado.contains(".")) {
                        resultadoFormatado = resultadoFormatado.replace(".", ",");
                    }
                    binding.contentPrincipal.textSaldo.setText(String.format("R$ %s", resultadoFormatado));
                    binding.contentPrincipal.textSaudacao.setText(String.format("Olá, %s", usuario.getNome()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configuraCalendarView() {
        calendarView.setTitleMonths(meses);
        calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        CalendarDay dataBase = calendarView.getCurrentDate();
        iniciaMesAnoSelecionado(dataBase);
        calendarView.setOnMonthChangedListener((widget, date) -> {
            iniciaMesAnoSelecionado(date);
            database.removeEventListener(valueEventListenerMovimentacoes);
            recuperaMovimentacoes();
        });
    }

    private void iniciaMesAnoSelecionado(CalendarDay dataBase) {
        if (dataBase.getMonth() < 10) {
            mesAnoSelecionado = "0" + dataBase.getMonth() + "" + dataBase.getYear();
        } else {
            mesAnoSelecionado = dataBase.getMonth() + "" + dataBase.getYear();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuSair) {
            ConfiguracaoFirebase.getAutenticacao().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        configuraCalendarView();
        recuperaMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        idUsuario.removeEventListener(evento);
        database.removeEventListener(valueEventListenerMovimentacoes);
    }
}