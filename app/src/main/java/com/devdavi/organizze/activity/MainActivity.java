package com.devdavi.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devdavi.organizze.R;
import com.devdavi.organizze.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private ActivityMainBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button cadastrese;

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

    public void btCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}