package utpDesarrolloMovil.demo.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonaDTO {
    private int id;
    @NotNull
    @NotBlank
    private String nombre;
    @NotNull
    @NotBlank
    private String apellido;
    @Positive
    @NotNull
    @Min(value = 10000000, message = "El número de tarjeta debe ser de al menos 8 dígitos")
    @Max(value = 99999999, message = "El número de tarjeta no puede superar los 8 dígitos")
    private int dni;
    @Positive
    @NotNull
    @Min(value = 10, message = "La edad minima es 10 años")
    @Max(value = 90, message = "La edad máxima es 90 años")
    private int edad;

}
