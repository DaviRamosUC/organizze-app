package com.devdavi.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.devdavi.organizze.R;
import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityDespesasBinding;
import com.devdavi.organizze.model.Movimentacao;
import com.devdavi.organizze.utils.Base64Custom;
import com.devdavi.organizze.utils.DateCustom;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class DespesasActivity extends AppCompatActivity {

    private ActivityDespesasBinding binding;
    private Movimentacao movimentacao;
    private String editData, editCategoria, editDescricao;
    private Double valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDespesasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Preenche o campo data com a data atual
        binding.editData.setText(DateCustom.dataAtual());

        binding.fab.setOnClickListener(view -> {
            String quantia = binding.campoValor.getText().toString();
            valor = quantia.isEmpty() ? 0 : Double.parseDouble(quantia);
            if (valor == 0) {
                Toast.makeText(getApplicationContext(), "Primeiro informe o valor", Toast.LENGTH_LONG)
                        .show();
            } else {
                editData = Objects.requireNonNull(binding.editData.getText()).toString();
                editCategoria = Objects.requireNonNull(binding.editCategoria.getText()).toString();
                editDescricao = Objects.requireNonNull(binding.editDescricao.getText()).toString();
                movimentacao = new Movimentacao(editData, editCategoria, editDescricao, "d", valor);
                movimentacao.salvar();
                finish();
            }
        });
    }
}