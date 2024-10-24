package utpDesarrolloMovil.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.dto.AuthLoginRequest;
import utpDesarrolloMovil.demo.dto.AuthResponse;
import utpDesarrolloMovil.demo.model.Usuario;
import utpDesarrolloMovil.demo.repository.UsuarioRepository;
import utpDesarrolloMovil.demo.util.JwtUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

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

    public AuthResponse loginRequest(AuthLoginRequest authLoginRequest){
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();
        Authentication authentication = this.authenticate(username, password); //Segundo paso, guardamos ese objeto authenticado en el sec. contex. holder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accesstoken = jwtUtils.createToken(authentication); //le generamos un token a ese objeto
        AuthResponse authResponse = new AuthResponse(username, "User loged Succesfully", accesstoken, true);
        return authResponse;

    }
    public Authentication authenticate(String username, String password){ //Primer paso, autenticar las credenciales con las de la BD
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Credenciales invalidas o este usuario no existe en la bd");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        //si son correctas y el usuario existe, creame un objeto authenticado
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
