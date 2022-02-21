package com.devdavi.organizze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.devdavi.organizze.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

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
                        .canGoForward(false)
                        .build()
        );


    }
}