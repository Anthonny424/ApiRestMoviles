package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.dto.TicketDTO;
import utpDesarrolloMovil.demo.model.Ticket;

import java.util.List;

public interface ITicket {
    List<TicketDTO> getTicketPorId(int id);
    void guardarTicket(Ticket ticket);
}
