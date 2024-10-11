package utpDesarrolloMovil.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Usuario;
import utpDesarrolloMovil.demo.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findUserByUsername(username).get();

        //Convertir ese usuario de bd a User (obj de spring sec.)
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>(); //Spring security solo entiende esta coleccion
        usuario.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))); //Pasamos todos los roles a la que solo entiende Spring Sec.

        usuario.getRoles().stream()
                .flatMap(role -> role.getPermisos().stream())
                .forEach(permiso -> authorityList.add(new SimpleGrantedAuthority(permiso.getName()))); //Pasando el Set<> de permisos a spring sec.
        return new User(usuario.getUsername()
                ,usuario.getContrasena()
                ,usuario.isEnable()
                , usuario.isAccountNoExpired()
                ,usuario.isCredentialNoExpired()
                , usuario.isAccountNoLocked(),
                authorityList
                ); //Asi convertimos todo el usuario de nuestra bd a un objeto que entiende Spring sec.
    }
}
