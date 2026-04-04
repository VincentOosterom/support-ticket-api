package nl.ticketsystem.mapper;

import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper implements DTOMapper<TicketResponseDTO, TicketRequestDTO, Ticket> {

    @Override
    public TicketResponseDTO mapToDto(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUserId(ticket.getUser().getId());
        return dto;
    }

    @Override
    public List<TicketResponseDTO> mapToDto(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Ticket mapToEntity(TicketRequestDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setSubject(dto.getSubject());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticket;
    }
}
