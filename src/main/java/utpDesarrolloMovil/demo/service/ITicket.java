package utpDesarrolloMovil.demo.service;

import utpDesarrolloMovil.demo.model.Ticket;

import java.util.List;

public interface ITicket {
    List<Ticket> getTicketPorId(int id);
    void guardarTicket(Ticket ticket);
}
