package nl.ticketsystem.mapper;

import nl.ticketsystem.dto.history.TicketHistoryRequestDTO;
import nl.ticketsystem.dto.history.TicketHistoryResponseDTO;
import nl.ticketsystem.model.TicketHistory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketHistoryMapper implements DTOMapper<TicketHistoryResponseDTO, TicketHistoryRequestDTO, TicketHistory> {

    @Override
    public TicketHistoryResponseDTO mapToDto(TicketHistory ticketHistory) {
        TicketHistoryResponseDTO dto = new TicketHistoryResponseDTO();
        dto.setId(ticketHistory.getId());
        dto.setOldStatus(ticketHistory.getOldStatus());
        dto.setNewStatus(ticketHistory.getNewStatus());
        dto.setChangedAt(ticketHistory.getChangedAt());
        dto.setTicketId(ticketHistory.getTicket().getId());
        dto.setChangeById(ticketHistory.getChangedBy().getId());
        return dto;
    }

    @Override
    public List<TicketHistoryResponseDTO> mapToDto(List<TicketHistory> ticketHistories) {
        return ticketHistories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TicketHistory mapToEntity(TicketHistoryRequestDTO dto) {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setOldStatus(dto.getOldStatus());
        ticketHistory.setNewStatus(dto.getNewStatus());
        ticketHistory.setChangedAt(LocalDateTime.now());
        return ticketHistory;
    }
}
