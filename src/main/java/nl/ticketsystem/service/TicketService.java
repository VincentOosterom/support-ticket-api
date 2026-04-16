package nl.ticketsystem.service;

import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.helpers.TicketStatusValidator;
import nl.ticketsystem.mapper.TicketMapper;
import nl.ticketsystem.model.*;
import nl.ticketsystem.repository.TicketHistoryRepository;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static nl.ticketsystem.model.TicketStatus.CLOSED;

@Service
public class TicketService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketStatusValidator ticketStatusValidator;
    private final TicketHistoryRepository ticketHistoryRepository;

    public TicketService(UserRepository userRepository, TicketRepository ticketRepository, TicketMapper ticketMapper, TicketStatusValidator ticketStatusValidator, TicketHistoryRepository ticketHistoryRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketStatusValidator = ticketStatusValidator;
        this.ticketHistoryRepository = ticketHistoryRepository;

    }

    public List<TicketResponseDTO> getAllTickets(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_CUSTOMER"));

        if (isCustomer) {
            User user = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));
            return ticketMapper.mapToDto(ticketRepository.findByUserAndStatusNot(user, CLOSED));
        }

        return ticketMapper.mapToDto(ticketRepository.findByStatusNot(CLOSED));
    }

    public TicketResponseDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));
        return ticketMapper.mapToDto(ticket);
    }

    public TicketResponseDTO createTicket(TicketRequestDTO dto, String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));
        Ticket ticket = ticketMapper.mapToEntity(dto);
        ticket.setUser(user);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.mapToDto(savedTicket);
    }

    public List<TicketResponseDTO> getTicketByStatus(TicketStatus status, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        if (status == CLOSED && !isAdmin) {
            throw new RuntimeException("Alleen een admin mag gesloten tickets zien");
        }
        return ticketMapper.mapToDto(ticketRepository.findByStatus(status));
    }

    public List<TicketResponseDTO> getTicketByPriority(Priority priority) {
        return ticketMapper.mapToDto(ticketRepository.findByPriority(priority));
    }

    public List<TicketResponseDTO> getTicketByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));
        return ticketMapper.mapToDto(ticketRepository.findByUser(user));
    }


    public TicketResponseDTO updateTicketStatus(Long id, TicketStatus newStatus, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();
        User changedBy = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));

        ticketStatusValidator.validate(ticket.getStatus(), newStatus);

        TicketHistory history = new TicketHistory();
        history.setOldStatus(ticket.getStatus());
        history.setNewStatus(newStatus);
        history.setChangedAt(LocalDateTime.now());
        history.setTicket(ticket);
        history.setChangedBy(changedBy);
        ticketHistoryRepository.save(history);

        if (newStatus == CLOSED) {
            ticket.setClosedAt(LocalDateTime.now());
        }

        ticket.setStatus(newStatus);
        return ticketMapper.mapToDto(ticketRepository.save(ticket));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public double getAverageDaysToClose() {
        List<Ticket> closedTickets = ticketRepository.findByStatus(TicketStatus.CLOSED);

        if (closedTickets.isEmpty()) {
            return 0;
        }

        long totalDays = 0;
        for (Ticket ticket : closedTickets) {
            if (ticket.getClosedAt() != null) {
                totalDays += ChronoUnit.DAYS.between(ticket.getCreatedAt(), ticket.getClosedAt());
            }
        }

        return (double) totalDays / closedTickets.size();
    }
}
