package utpDesarrolloMovil.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Permiso;
import utpDesarrolloMovil.demo.repository.PermisoRepository;

@Service
public class IPermisoImpl implements IPermiso{

    @Autowired
    private PermisoRepository repository;


    @Override
    public Permiso buscarPorId(int id) {
        return repository.findById(id);
    }
}
