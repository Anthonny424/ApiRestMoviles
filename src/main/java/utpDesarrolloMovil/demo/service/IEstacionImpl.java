package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Estacion;
import utpDesarrolloMovil.demo.repository.EstacionRepository;

import java.util.List;

@Service
public class IEstacionImpl implements IEstacion{

    @Autowired
    private EstacionRepository repository;

    @Override
    public List<Estacion> getEstaciones() {
        return repository.findAll();
    }

}
