package utpDesarrolloMovil.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.dto.TicketDTO;
import utpDesarrolloMovil.demo.model.Ticket;
import utpDesarrolloMovil.demo.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ITIcketImpl implements ITicket{

    @Autowired
    private TicketRepository repository;


    @Override
    public List<TicketDTO> getTicketPorId(int id) {
        ModelMapper modelMapper = new ModelMapper();
        List<Ticket> tickets = repository.findAllByTarjetaId(id);
        List<TicketDTO> ticketDTOS = new ArrayList<>();
        for(Ticket ticket : tickets){
            ticketDTOS.add(modelMapper.map(ticket, TicketDTO.class));
        }
        return ticketDTOS;
    }

    @Override
    public void guardarTicket(Ticket ticket) {
        repository.save(ticket);
    }
}
