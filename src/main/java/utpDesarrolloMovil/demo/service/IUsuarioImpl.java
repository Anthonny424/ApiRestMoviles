package utpDesarrolloMovil.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.dto.*;
import utpDesarrolloMovil.demo.model.*;
import utpDesarrolloMovil.demo.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public Usuario findUserByIdNotDto(int id) {
        return repository.findById(id);
    }

    @Override
    public UsuarioDTO getUsuarioPorId(int id) {
        Usuario usuario = repository.findById(id);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Usuario.class, UsuarioDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Usuario::getTarjeta, UsuarioDTO::setTarjetaDTO);
                    mapper.map(src -> src.getTarjeta().getTarifa(),
                            (dest, value) -> dest.getTarjetaDTO().setTarifaDTO((TarifaDTO) value));
                    mapper.map(Usuario::getPersona, UsuarioDTO::setPersonaDTO);
                });
        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        return usuarioDTO;
    }

    @Override
    public Usuario GuardarUser(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    public UsuarioDTO SaveUsuarioCompleto(UsuarioCreateDTO usuarioDTO) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuarioDTO.getUsername());
        nuevoUsuario.setContrasena(passwordEncoder.encode(usuarioDTO.getPassword()));
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
        tarjeta.setNrotarjeta(usuarioDTO.getTarjetaDTO().getNrotarjeta());
        Tarifa tarifaexistente = tarifaService.buscarporId(1);//Tarifa PASAJE COMPLETO
        tarjeta.setTarifa(tarifaexistente);
        tarjeta.setUsuario(nuevoUsuario);
        nuevoUsuario.setTarjeta(tarjeta);

        //Registrar persona asociada a este usuario
        Persona persona = new Persona();
        persona.setNombre(usuarioDTO.getPersonaDTO().getNombre());
        persona.setApellido(usuarioDTO.getPersonaDTO().getApellido());
        persona.setDni(usuarioDTO.getPersonaDTO().getDni());
        persona.setEdad(usuarioDTO.getPersonaDTO().getEdad());
        persona.setUsuario(nuevoUsuario);
        nuevoUsuario.setPersona(persona);
        repository.save(nuevoUsuario);

        Usuario usuario = repository.findUserByUsername(usuarioDTO.getUsername()).get();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Usuario.class, UsuarioDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Usuario::getTarjeta, UsuarioDTO::setTarjetaDTO);
                    mapper.map(src -> src.getTarjeta().getTarifa(),
                            (dest, value) -> dest.getTarjetaDTO().setTarifaDTO((TarifaDTO) value));
                    mapper.map(Usuario::getPersona, UsuarioDTO::setPersonaDTO);
                });
        UsuarioDTO usuarioFound = modelMapper.map(usuario, UsuarioDTO.class);
        return usuarioFound;
    }

    @Override
    public UsuarioDTO updateUsuario(Usuario usuarioEncontrado, String username, String contrasena, int nrotarjeta) {
        Usuario usuario1 = usuarioEncontrado;
        usuario1.setId(usuarioEncontrado.getId());
        usuario1.setUsername(username);
        usuario1.setContrasena(passwordEncoder.encode(contrasena));
        Tarjeta tarjeta = usuario1.getTarjeta();
        tarjeta.setNrotarjeta(nrotarjeta);
        usuario1.setTarjeta(tarjeta);
        Usuario usuario = repository.save(usuario1);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Usuario.class, UsuarioDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Usuario::getTarjeta, UsuarioDTO::setTarjetaDTO);
                    mapper.map(src -> src.getTarjeta().getTarifa(),
                            (dest, value) -> dest.getTarjetaDTO().setTarifaDTO((TarifaDTO) value));
                    mapper.map(Usuario::getPersona, UsuarioDTO::setPersonaDTO);
                });
        UsuarioDTO usuarioUpdated = modelMapper.map(usuario, UsuarioDTO.class);
        return usuarioUpdated;
    }

    @Override
    public Usuario encontrarPorUsename(String username) {
        return repository.findUserByUsername(username).get();
    }


}
