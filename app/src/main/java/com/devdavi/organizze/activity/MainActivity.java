package com.devdavi.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.devdavi.organizze.R;
import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;
    private ActivityMainBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        addSlide(
                new FragmentSlide.Builder()
                        .background(R.color.white)
                        .fragment(R.layout.intro_1)
                        .canGoBackward(false)
                        .build()
        );
        addSlide(
                new FragmentSlide.Builder()
                        .background(R.color.white)
                        .fragment(R.layout.intro_2)
                        .build()
        );
        addSlide(
                new FragmentSlide.Builder()
                        .background(R.color.white)
                        .fragment(R.layout.intro_3)
                        .build()
        );
        addSlide(
                new FragmentSlide.Builder()
                        .background(R.color.white)
                        .fragment(R.layout.intro_4)
                        .build()
        );

        addSlide(
                new FragmentSlide.Builder()
                        .background(R.color.white)
                        .fragment(R.layout.intro_cadastro)
                        .canGoForward(false)
                        .build()
        );


    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        if (autenticacao.getCurrentUser() != null)
            abrirTelaPrincipal();
    }

    private void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
    }

    public void btCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}