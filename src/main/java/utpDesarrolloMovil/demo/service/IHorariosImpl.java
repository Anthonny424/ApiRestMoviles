package utpDesarrolloMovil.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.dto.HorarioDTO;
import utpDesarrolloMovil.demo.model.Horario;
import utpDesarrolloMovil.demo.repository.HorarioRepository;

import java.util.ArrayList;
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
    public List<HorarioDTO> getByEstacion(Integer id) {
        ModelMapper modelMapper = new ModelMapper();
        List<Horario> horarios = repository.findAllByEstacionId(id);
        List<HorarioDTO> horarioDTOS = new ArrayList<>();
        for (Horario horario: horarios){
            horarioDTOS.add(modelMapper.map(horario, HorarioDTO.class));
        }
        return horarioDTOS;
    }
}
