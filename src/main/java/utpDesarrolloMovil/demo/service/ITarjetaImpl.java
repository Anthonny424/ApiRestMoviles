package utpDesarrolloMovil.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.dto.TarjetaDTO;
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
    public TarjetaDTO buscarPorIdWithJPQL(int id) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Tarjeta.class, TarjetaDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Tarjeta::getTarifa, TarjetaDTO::setTarifaDTO);
                });
        Tarjeta tarjeta = repository.findById(id).get();
        TarjetaDTO tarjetaDTO = modelMapper.map(tarjeta, TarjetaDTO.class);
        return tarjetaDTO;
    }

    @Override
    public Tarjeta buscarPorIdNoDTO(int id) {
        return repository.findById(id).get();
    }

    @Override
    public void guardarTarjeta(Tarjeta tarjeta) {
        repository.save(tarjeta);
    }
}
