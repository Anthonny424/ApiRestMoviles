package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.dto.EstacionDTO;
import utpDesarrolloMovil.demo.model.Estacion;

import java.util.List;

public interface IEstacion {
    List<EstacionDTO> getEstaciones();
}
