package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.model.Tarjeta;

import java.util.List;
import java.util.Optional;

public interface ITarjeta {


    List<Tarjeta> getAllTarjetas();
    Optional<Tarjeta> buscarPorIdWithJPQL(int id);
    void guardarTarjeta(Tarjeta tarjeta);

}
