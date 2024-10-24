package utpDesarrolloMovil.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TarjetaDTO {
    private int id;
    private double saldo;
    @Positive
    @NotNull
    @Min(value = 10000000, message = "El número de tarjeta debe ser de al menos 8 dígitos")
    @Max(value = 99999999, message = "El número de tarjeta no puede superar los 8 dígitos")
    private int nrotarjeta;
    private TarifaDTO tarifaDTO;
}
