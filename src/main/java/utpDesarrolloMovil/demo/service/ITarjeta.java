package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.dto.TarjetaDTO;
import utpDesarrolloMovil.demo.model.Tarjeta;

import java.util.List;
import java.util.Optional;

public interface ITarjeta {


    List<Tarjeta> getAllTarjetas();
    TarjetaDTO buscarPorIdUserWithJPQL(int id);
    Tarjeta buscarPorIdNoDTO(int id);
    void guardarTarjeta(Tarjeta tarjeta);

}
