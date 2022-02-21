package com.devdavi.organizze.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Validadores {

    public static List<String> verificaCampo(String nome, String textoEmail, String textoSenha) {
        List<String> retorno = new ArrayList<>();
        if (nome.isEmpty())
            retorno.add("nome");
        if (textoEmail.isEmpty())
            retorno.add("email");
        if (textoSenha.isEmpty())
            retorno.add("senha");
        return retorno;
    }
    public static List<String> verificaCampo(String textoEmail, String textoSenha) {
        List<String> retorno = new ArrayList<>();
        if (textoEmail.isEmpty())
            retorno.add("email");
        if (textoSenha.isEmpty())
            retorno.add("senha");
        return retorno;
    }

    @NonNull
    public static String criaMensagem(List<String> camposVazios) {
        String mensagem;
        StringBuilder sb = new StringBuilder();
        for (String campo : camposVazios) {
            sb.append(campo).append(", ");
        }
        mensagem = sb.toString().trim();
        return mensagem.substring(0, mensagem.length() - 1);
    }
}
