package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuario {
    Usuario getUsuarioPorId(int id);

    Usuario GuardarUser(Usuario usuario);

    Usuario SaveUsuarioCompleto(Usuario usuario);

    Usuario updateUsuario(Usuario usuario, String username, String contrasena, int nrotarjeta);

    Usuario encontrarPorUsename(String username);

}