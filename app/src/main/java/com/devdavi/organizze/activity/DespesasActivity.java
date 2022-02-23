package com.devdavi.organizze.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityDespesasBinding;
import com.devdavi.organizze.model.Movimentacao;
import com.devdavi.organizze.model.Usuario;
import com.devdavi.organizze.utils.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DespesasActivity extends AppCompatActivity {

    private ActivityDespesasBinding binding;
    private Movimentacao movimentacao;
    private String editData, editCategoria, editDescricao;
    private Double editValor;
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDespesasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Preenche o campo data com a data atual
        binding.editData.setText(DateCustom.dataAtual());
        recuperarDespesaTotal();

        binding.fab.setOnClickListener(view -> {
            if (validarCamposDespesa()) {
                editValor = Double.parseDouble(binding.campoValor.getText().toString());
                editData = Objects.requireNonNull(binding.editData.getText()).toString();
                editCategoria = Objects.requireNonNull(binding.editCategoria.getText()).toString();
                editDescricao = Objects.requireNonNull(binding.editDescricao.getText()).toString();
                movimentacao = new Movimentacao(editData, editCategoria, editDescricao, "d", editValor);
                Double despesaAtualizada = despesaTotal + editValor;
                atualizarDespesa(despesaAtualizada);
                movimentacao.salvar();
                finish();
            }
        });
    }

    private boolean validarCamposDespesa() {
        if (binding.campoValor.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Primeiro informe o valor", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (Objects.requireNonNull(binding.editData.getText()).toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, informe a data", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (Objects.requireNonNull(binding.editCategoria.getText()).toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, informe a categoria", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        if (Objects.requireNonNull(binding.editDescricao.getText()).toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, informe a categoria", Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    public void recuperarDespesaTotal() {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getIdUsuarioChild();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                assert usuario != null;
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarDespesa(Double despesaAtualizada) {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getIdUsuarioChild();
        usuarioRef.child("despesaTotal").setValue(despesaAtualizada);

    }
}