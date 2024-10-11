package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.*;
import utpDesarrolloMovil.demo.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IUsuarioImpl implements IUsuario{

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ITarifa tarifaService;

    @Autowired
    private IRol rolService;

    @Autowired
    private IPermiso permisoService;


    @Override
    public Usuario getUsuarioPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public Usuario GuardarUser(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    public Usuario SaveUsuarioCompleto(Usuario usuario) {
        Usuario nuevoUsuario = usuario;
        nuevoUsuario.setContrasena(passwordEncoder.encode(nuevoUsuario.getContrasena()));

        nuevoUsuario.setEnable(true);
        nuevoUsuario.setAccountNoExpired(true);
        nuevoUsuario.setAccountNoLocked(true);
        nuevoUsuario.setCredentialNoExpired(true);

        //Crear permisos por default (READ)
        Set<Permiso> permisos = new HashSet<>();
        Permiso permiso = permisoService.buscarPorId(2); //READ
        permisos.add(permiso);

        //Crear rol por default USER
        Set<Rol> roles = new HashSet<>();
        Rol rol = rolService.BuscarPorId(2); //USER
        rol.setPermisos(permisos);
        roles.add(rol);
        nuevoUsuario.setRoles(roles);

        //Asociar ID de tarjeta al usuario y tarifa por defecto
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setSaldo(1.50); //Le pongo 1.50 por defecto pq no sabemos el monto de cada tarjeta de lima
        tarjeta.setNrotarjeta(usuario.getTarjeta().getNrotarjeta());
        Tarifa tarifaexistente = tarifaService.buscarporId(1);//Tarifa PASAJE COMPLETO
        tarjeta.setTarifa(tarifaexistente);
        tarjeta.setUsuario(nuevoUsuario);
        nuevoUsuario.setTarjeta(tarjeta);

        //Registrar persona asociada a este usuario
        Persona persona = new Persona();
        persona.setNombre(nuevoUsuario.getPersona().getNombre());
        persona.setApellido(nuevoUsuario.getPersona().getApellido());
        persona.setDni(nuevoUsuario.getPersona().getDni());
        persona.setEdad(nuevoUsuario.getPersona().getEdad());
        persona.setUsuario(nuevoUsuario);
        nuevoUsuario.setPersona(persona);

        return GuardarUser(nuevoUsuario);
    }

    @Override
    public Usuario updateUsuario(Usuario usuarioEncontrado, String username, String contrasena, int nrotarjeta) {
        Usuario usuario1 = usuarioEncontrado;
        usuario1.setId(usuarioEncontrado.getId());
        usuario1.setUsername(username);
        usuario1.setContrasena(passwordEncoder.encode(contrasena));
        Tarjeta tarjeta = usuario1.getTarjeta();
        tarjeta.setNrotarjeta(nrotarjeta);
        usuario1.setTarjeta(tarjeta);

        return repository.save(usuario1);
    }
}
