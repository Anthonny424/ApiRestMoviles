package utpDesarrolloMovil.demo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class UsuarioDTO {
    private int id;
    private String username;
    private TarjetaDTO tarjetaDTO;
    private PersonaDTO personaDTO;
}
