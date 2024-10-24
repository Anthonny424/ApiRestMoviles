package utpDesarrolloMovil.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioCreateDTO {

    private int id;
    private String username;

    @NotNull(message = "La contraseña es requerida")
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String password;
    private TarjetaDTO tarjetaDTO;
    private PersonaDTO personaDTO;
}
