package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.dto.HorarioDTO;
import utpDesarrolloMovil.demo.model.Horario;

import java.util.List;

public interface IHorarios {
    List<Horario> getAllHorarios();

    List<HorarioDTO> getByEstacion(Integer id);
}
