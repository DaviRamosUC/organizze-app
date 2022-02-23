package com.devdavi.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.devdavi.organizze.R;
import com.devdavi.organizze.databinding.ActivityDespesasBinding;
import com.devdavi.organizze.utils.DateCustom;

public class DespesasActivity extends AppCompatActivity {

    private ActivityDespesasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDespesasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Preenche o campo data com a data atual
        binding.editData.setText(DateCustom.dataAtual());

        binding.fab.setOnClickListener(view -> {

        });
    }
}