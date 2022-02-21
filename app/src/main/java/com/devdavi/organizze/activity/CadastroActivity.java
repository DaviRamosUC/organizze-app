package com.devdavi.organizze.activity;

import static com.devdavi.organizze.utils.Validadores.criaMensagem;
import static com.devdavi.organizze.utils.Validadores.verificaCampo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityCadastroBinding;
import com.devdavi.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCadastrar.setOnClickListener(view -> {

            String textoNome = binding.editNome.getText().toString();
            String textoEmail = binding.editEmail.getText().toString();
            String textoSenha = binding.editSenha.getText().toString();

            List<String> camposVazios = verificaCampo(textoNome, textoEmail, textoSenha);

            if (camposVazios.size() > 0) {
                String mensagem = criaMensagem(camposVazios);
                Toast.makeText(getApplicationContext(), "Preencher " + mensagem + " acima", Toast.LENGTH_LONG)
                        .show();
            } else {
                usuario = new Usuario(textoNome, textoEmail, textoSenha);
                cadastrarUsuario();
            }

        });
    }

    private void cadastrarUsuario() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        String excecao;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            excecao = "Digite uma senha mais forte";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Por favor, digite um email válido";
                        } catch (FirebaseAuthUserCollisionException e) {
                            excecao = "Esta conta já foi cadastrada";
                        } catch (Exception e) {
                            excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

}