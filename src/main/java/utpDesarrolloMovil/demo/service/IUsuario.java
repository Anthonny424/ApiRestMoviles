package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.dto.UsuarioCreateDTO;
import utpDesarrolloMovil.demo.dto.UsuarioDTO;
import utpDesarrolloMovil.demo.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuario {

    Usuario findUserByIdNotDto(int id);

    UsuarioDTO getUsuarioPorId(int id);

    Usuario GuardarUser(Usuario usuario);

    UsuarioDTO SaveUsuarioCompleto(UsuarioCreateDTO usuario);

    UsuarioDTO updateUsuario(Usuario usuario, String username, String contrasena, int nrotarjeta);

    Usuario encontrarPorUsename(String username);

}