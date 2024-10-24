package utpDesarrolloMovil.demo.dto;

import lombok.Data;

@Data
public class TarifaDTO {
    private int id;
    private String tipo;
    private String descripcion;
    private double monto;
}
