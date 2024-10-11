package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Persona;
import utpDesarrolloMovil.demo.repository.PersonaRepository;

import java.util.List;


@Service
public class IPersonaImpl implements IPersona {

    @Autowired
    private PersonaRepository repository;

    @Override
    public List<Persona> getAllPersona() {
        return repository.findAll();
    }
}
