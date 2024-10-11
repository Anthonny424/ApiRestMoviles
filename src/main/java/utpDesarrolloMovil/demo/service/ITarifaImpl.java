package utpDesarrolloMovil.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Tarifa;
import utpDesarrolloMovil.demo.repository.TarifaRepository;

import java.lang.annotation.Annotation;

@Service
public class ITarifaImpl implements ITarifa {

    @Autowired
    private TarifaRepository repository;


    @Override
    public Tarifa buscarporId(int id) {
        return repository.findById(id);
    }
}
