package nl.ticketsystem.service;

import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.mapper.TicketMapper;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(UserRepository userRepository, TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    public List<TicketResponseDTO> getAllTickets() {
        return ticketMapper.mapToDto(ticketRepository.findAll());
    }

    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket niet gevonden"));
        return ticketMapper.mapToDto(ticket);
    }

    public TicketResponseDTO createTicket(TicketRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User niet gevonden"));
        Ticket ticket = ticketMapper.mapToEntity(dto);
        ticket.setUser(user);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.mapToDto(savedTicket);
    }

    public TicketResponseDTO updateTicketStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket niet gevonden"));
        ticket.setStatus(newStatus);
        return ticketMapper.mapToDto(ticketRepository.save(ticket));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
