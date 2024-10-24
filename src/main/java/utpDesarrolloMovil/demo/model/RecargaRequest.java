package utpDesarrolloMovil.demo.model;

import lombok.Data;

@Data
public class RecargaRequest {
    private int iduser;
    private double monto;
    private int idtarjeta;
}
