package com.devdavi.organizze.config;

import com.devdavi.organizze.utils.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static FirebaseDatabase database;

    public static FirebaseAuth getAutenticacao() {
        if (autenticacao == null)
            autenticacao = FirebaseAuth.getInstance();
        return autenticacao;
    }

    public static DatabaseReference getDatabase() {
        if (database == null)
            database = FirebaseDatabase.getInstance();
        return database.getReference();
    }

    public static DatabaseReference getIdUsuarioChild(){
        String emailUsuario = Objects.requireNonNull(getAutenticacao().getCurrentUser()).getEmail();
        assert emailUsuario != null;
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        return getDatabase().child("usuarios").child(idUsuario);
    }

}
