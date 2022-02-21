package com.devdavi.organizze.activity;

import static com.devdavi.organizze.utils.Validadores.criaMensagem;
import static com.devdavi.organizze.utils.Validadores.verificaCampo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityLoginBinding;
import com.devdavi.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonEntrar.setOnClickListener(view -> {
            String textoEmail = binding.editEmailLogin.getText().toString();
            String textoSenha = binding.editSenhaLogin.getText().toString();

            List<String> camposVazios = verificaCampo(textoEmail, textoSenha);

            if (camposVazios.size() > 0) {
                String mensagem = criaMensagem(camposVazios);
                Toast.makeText(getApplicationContext(), "Preencher " + mensagem + " acima", Toast.LENGTH_LONG)
                        .show();
            } else {
                usuario = new Usuario(textoEmail, textoSenha);
                validarLogin();
            }
        });
    }

    private void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        abrirTelaPrincipal();
                    } else {
                        String excecao;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Email e/ou senha errados";
                        } catch (FirebaseAuthInvalidUserException e) {
                            excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                        } catch (Exception e) {
                            excecao = "Erro ao efetuar login: " + e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}