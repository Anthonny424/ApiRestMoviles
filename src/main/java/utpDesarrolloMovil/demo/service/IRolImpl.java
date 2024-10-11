package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Rol;
import utpDesarrolloMovil.demo.repository.RolRepository;

@Service
public class IRolImpl implements IRol{

    @Autowired
    private RolRepository repository;

    @Override
    public Rol BuscarPorId(int id) {
        return repository.findById(id);
    }
}
