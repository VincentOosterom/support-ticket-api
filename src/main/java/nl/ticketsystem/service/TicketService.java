package nl.ticketsystem.service;

import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.helpers.TicketStatusValidator;
import nl.ticketsystem.mapper.TicketMapper;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static nl.ticketsystem.model.TicketStatus.OPEN;

@Service
public class TicketService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketStatusValidator ticketStatusValidator;

    public TicketService(UserRepository userRepository, TicketRepository ticketRepository, TicketMapper ticketMapper, TicketStatusValidator ticketStatusValidator) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketStatusValidator = ticketStatusValidator;
    }

    public List<TicketResponseDTO> getAllTickets() {
        return ticketMapper.mapToDto(ticketRepository.findAll());
    }

    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));
        return ticketMapper.mapToDto(ticket);
    }

    public TicketResponseDTO createTicket(TicketRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));
        Ticket ticket = ticketMapper.mapToEntity(dto);
        ticket.setUser(user);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.mapToDto(savedTicket);
    }

    public List<TicketResponseDTO> getTicketByStatus(TicketStatus status) {
        return ticketMapper.mapToDto(ticketRepository.findByStatus(status));
    }

    public TicketResponseDTO updateTicketStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));

        ticketStatusValidator.validate(ticket.getStatus(), newStatus);

        ticket.setStatus(newStatus);
        return ticketMapper.mapToDto(ticketRepository.save(ticket));
    }


    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
