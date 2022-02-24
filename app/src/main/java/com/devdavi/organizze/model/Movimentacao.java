package com.devdavi.organizze.model;

import com.devdavi.organizze.config.ConfiguracaoFirebase;
import com.devdavi.organizze.utils.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;

    public Movimentacao() {
    }

    public Movimentacao(String data, String categoria, String descricao, String tipo, Double valor) {
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Movimentacao{");
        sb.append("data='").append(data).append('\'');
        sb.append(", categoria='").append(categoria).append('\'');
        sb.append(", descricao='").append(descricao).append('\'');
        sb.append(", tipo='").append(tipo).append('\'');
        sb.append(", valor=").append(valor);
        sb.append('}');
        return sb.toString();
    }

    public void salvar() {
        FirebaseAuth auth = ConfiguracaoFirebase.getAutenticacao();
        DatabaseReference database = ConfiguracaoFirebase.getDatabase();
        String email = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()));
        database.child("movimentacao")
                .child(email)
                .child(this.data.replace("/", "").substring(2, data.length() - 2))
                .push()
                .setValue(this);
    }
}
