package utpDesarrolloMovil.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.dto.EstacionDTO;
import utpDesarrolloMovil.demo.model.Estacion;
import utpDesarrolloMovil.demo.repository.EstacionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class IEstacionImpl implements IEstacion{

    @Autowired
    private EstacionRepository repository;

    @Override
    public List<EstacionDTO> getEstaciones() {
        ModelMapper modelMapper = new ModelMapper();
        List<Estacion> estacions =  repository.findAll();
        List<EstacionDTO> estacionDTOS = new ArrayList<>();
        for (Estacion estacion : estacions){
          estacionDTOS.add(modelMapper.map(estacion, EstacionDTO.class));
        }
        return estacionDTOS;
    }

}
