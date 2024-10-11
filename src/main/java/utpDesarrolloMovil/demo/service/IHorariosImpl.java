package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Horario;
import utpDesarrolloMovil.demo.repository.HorarioRepository;

import java.util.List;

@Service
public class IHorariosImpl implements IHorarios{

    @Autowired
    private HorarioRepository repository;
    @Override
    public List<Horario> getAllHorarios() {
        return repository.findAll();
    }

    @Override
    public List<Horario> getByEstacion(Integer id) {
        return repository.findAllByEstacionId(id);
    }
}
