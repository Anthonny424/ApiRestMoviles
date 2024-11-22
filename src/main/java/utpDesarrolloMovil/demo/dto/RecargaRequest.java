package utpDesarrolloMovil.demo.dto;

import lombok.Data;

@Data
public class RecargaRequest {
    private int iduser;
    private double monto;
    private int idtarjeta;
}
