package com.devdavi.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
                            Log.d("dadosRetorno", "onDataChange: " + movimentacao.toString() + "\n");
                            movimentacoes.add(movimentacao);
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