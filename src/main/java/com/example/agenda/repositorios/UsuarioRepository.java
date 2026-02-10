package com.example.agenda.repositorios;

import com.example.agenda.entidades.Rol;
import com.example.agenda.entidades.Usuario;
import com.example.agenda.seguridad.PasswordEncryptor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioRepository {

    public List<Usuario> getUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        // Usuario ADMIN: aitor / 1234
        usuarios.add(new Usuario("aitor", PasswordEncryptor.encrypt("1234"), Rol.ADMIN));
        // Usuario USER: alicia / 1111
        usuarios.add(new Usuario("alicia", PasswordEncryptor.encrypt("1111"), Rol.USER));

        return usuarios;
    }
}