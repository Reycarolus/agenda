package com.example.agenda.controladores;

import com.example.agenda.entidades.Usuario;
import com.example.agenda.repositorios.UsuarioRepository;
import com.example.agenda.seguridad.Constans;
import com.example.agenda.seguridad.JWTAuthenticationConfig;
import com.example.agenda.seguridad.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.coyote.BadRequestException;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthtenticationConfig;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping("login")
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass) throws BadRequestException {

        List<Usuario> usuarios = usuarioRepository.getUsuarios();
        Usuario usuarioEncontrado = null;

        for (Usuario usuario : usuarios) {
            // Comprobamos usuario y desciframos la contraseña guardada para compararla con la recibida
            if (usuario.getUsername().equals(username) &&
                    PasswordEncryptor.decrypt(usuario.getEncryptedPass()).equals(encryptedPass)) {
                usuarioEncontrado = usuario;
                break;
            }
        }

        if (usuarioEncontrado == null) {
            throw new BadRequestException();
        }

        // Generamos el token incluyendo el ROL del usuario encontrado
        String token = jwtAuthtenticationConfig.getJWTToken(
                usuarioEncontrado.getUsername(),
                usuarioEncontrado.getRol());

        return token;
    }
}