package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Tarjeta;
import utpDesarrolloMovil.demo.repository.TarjetaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ITarjetaImpl implements ITarjeta {

    @Autowired
    private TarjetaRepository repository;

    @Override
    public List<Tarjeta> getAllTarjetas() {
        return repository.findAll();
    }

    @Override
    public Optional<Tarjeta> buscarPorIdWithJPQL(int id) {
        return repository.findById(id);
    }

    @Override
    public void guardarTarjeta(Tarjeta tarjeta) {
        repository.save(tarjeta);
    }
}
