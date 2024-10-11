package utpDesarrolloMovil.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.model.Ticket;
import utpDesarrolloMovil.demo.repository.TicketRepository;

import java.util.List;

@Service
public class ITIcketImpl implements ITicket{

    @Autowired
    private TicketRepository repository;


    @Override
    public List<Ticket> getTicketPorId(int id) {
        return repository.findAllByTarjetaId(id);
    }

    @Override
    public void guardarTicket(Ticket ticket) {
        repository.save(ticket);
    }
}
