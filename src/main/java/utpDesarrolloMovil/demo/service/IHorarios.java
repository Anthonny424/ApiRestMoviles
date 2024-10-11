package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.model.Horario;

import java.util.List;

public interface IHorarios {
    List<Horario> getAllHorarios();

    List<Horario> getByEstacion(Integer id);
}
