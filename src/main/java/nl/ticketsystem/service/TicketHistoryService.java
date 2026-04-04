package nl.ticketsystem.service;

import nl.ticketsystem.dto.history.TicketHistoryRequestDTO;
import nl.ticketsystem.dto.history.TicketHistoryResponseDTO;
import nl.ticketsystem.mapper.TicketHistoryMapper;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketHistory;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.TicketHistoryRepository;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketHistoryMapper ticketHistoryMapper;

    public TicketHistoryService(TicketHistoryRepository ticketHistoryRepository, TicketRepository ticketRepository, UserRepository userRepository, TicketHistoryMapper ticketHistoryMapper) {
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.ticketHistoryMapper = ticketHistoryMapper;
    }

    public List<TicketHistoryResponseDTO> getAllTicketHistories() {
        return ticketHistoryMapper.mapToDto(ticketHistoryRepository.findAll());
    }

    public TicketHistoryResponseDTO getTicketHistoryById(Long id) {
        TicketHistory ticketHistory = ticketHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketHistory niet gevonden"));
        return ticketHistoryMapper.mapToDto(ticketHistory);

    }

    public TicketHistoryResponseDTO createTicketHistory(TicketHistoryRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket niet gevonden"));
        User changedBy = userRepository.findById(dto.getChangeById())
                .orElseThrow(() -> new RuntimeException("User niet gevonden"));
        TicketHistory ticketHistory = ticketHistoryMapper.mapToEntity(dto);
        ticketHistory.setTicket(ticket);
        ticketHistory.setChangedBy(changedBy);
        TicketHistory savedTicketHistory = ticketHistoryRepository.save(ticketHistory);
        return ticketHistoryMapper.mapToDto(savedTicketHistory);
    }
}
