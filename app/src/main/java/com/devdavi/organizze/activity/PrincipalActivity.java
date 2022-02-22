package com.devdavi.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.devdavi.organizze.R;
import com.devdavi.organizze.databinding.ActivityPrincipalBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.menuReceita.setOnClickListener(view -> {
            startActivity(new Intent(this, ReceitasActivity.class));
        });

        binding.menuDespesa.setOnClickListener(view -> {
            startActivity(new Intent(this, DespesasActivity.class));
        });
    }

}