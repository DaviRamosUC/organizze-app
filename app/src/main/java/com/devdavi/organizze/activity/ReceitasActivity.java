package com.devdavi.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityReceitasBinding;
import com.devdavi.organizze.model.Movimentacao;
import com.devdavi.organizze.model.Usuario;
import com.devdavi.organizze.utils.DateCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ReceitasActivity extends AppCompatActivity {

    private ActivityReceitasBinding binding;
    private Movimentacao movimentacao;
    private String editData, editCategoria, editDescricao;
    private Double editValor;
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceitasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editCampoData.setText(DateCustom.dataAtual());
        recuperarReceitaTotal();

        binding.fab.setOnClickListener(view -> {
            if (validarCamposReceita()){
                editValor = Double.parseDouble(binding.editcampoValor.getText().toString());
                editData = Objects.requireNonNull(binding.editCampoData.getText()).toString();
                editCategoria = Objects.requireNonNull(binding.editCampoCategoria.getText()).toString();
                editDescricao = Objects.requireNonNull(binding.editCampoDescricao.getText()).toString();
                movimentacao = new Movimentacao(editData, editCategoria, editDescricao, "r", editValor);
                Double receitaAtualizada = receitaTotal + editValor;
                atualizarReceita(receitaAtualizada);
                movimentacao.salvar();
                finish();
            }
        });
    }

    private boolean validarCamposReceita() {
        if (binding.editcampoValor.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Primeiro informe o valor", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (Objects.requireNonNull(binding.editCampoData.getText()).toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, informe a data", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (Objects.requireNonNull(binding.editCampoCategoria.getText()).toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, informe a categoria", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (Objects.requireNonNull(binding.editCampoDescricao.getText()).toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, informe a categoria", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    public void recuperarReceitaTotal() {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getIdUsuarioChild();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                assert usuario != null;
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceita(Double receitaAtualizada) {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getIdUsuarioChild();
        usuarioRef.child("receitaTotal").setValue(receitaAtualizada);

    }
}