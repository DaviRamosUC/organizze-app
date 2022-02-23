package com.devdavi.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devdavi.organizze.R;
import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityPrincipalBinding;
import com.devdavi.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DecimalFormat;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private String dataPesquisa;
    private MaterialCalendarView calendarView;
    private FirebaseAuth auth;
    private DatabaseReference idUsuario;
    private DatabaseReference database;
    private ValueEventListener evento;

    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoTotal = 0.0;
    private final CharSequence meses[] = {
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

        auth = ConfiguracaoFirebase.getAutenticacao();
        idUsuario = ConfiguracaoFirebase.getIdUsuarioChild();
        database = ConfiguracaoFirebase.getDatabase();

        calendarView = binding.contentPrincipal.calendarView;

        binding.menuReceita.setOnClickListener(view -> {
            startActivity(new Intent(this, ReceitasActivity.class));
        });

        binding.menuDespesa.setOnClickListener(view -> {
            startActivity(new Intent(this, DespesasActivity.class));
        });

    }

    public void recuperarResumo() {
        evento = idUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoTotal = receitaTotal - despesaTotal;
                DecimalFormat df = new DecimalFormat("0.00");
                String resultadoFormatado = df.format(resumoTotal);
                if (resultadoFormatado.contains(".")) {
                    resultadoFormatado = resultadoFormatado.replace(".", ",");
                }
                binding.contentPrincipal.textSaldo.setText("R$ " + resultadoFormatado);
                binding.contentPrincipal.textSaudacao.setText("Olá, " + usuario.getNome());
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

        calendarView.setOnMonthChangedListener((widget, date) -> {
            dataPesquisa = date.getMonth() + "" + date.getYear();
        });
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
        configuraCalendarView();
        recuperarResumo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        idUsuario.removeEventListener(evento);
    }
}