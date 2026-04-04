package nl.ticketsystem.mapper;


import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentRequestDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentResponseDTO;
import nl.ticketsystem.model.TicketAssignment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketAssignmentMapper implements DTOMapper<TicketAssignmentResponseDTO, TicketAssignmentRequestDTO, TicketAssignment> {

    @Override
    public TicketAssignmentResponseDTO mapToDto(TicketAssignment ticketAssignment) {
        TicketAssignmentResponseDTO dto = new TicketAssignmentResponseDTO();
        dto.setId(ticketAssignment.getId());
        dto.setTicketId(ticketAssignment.getTicket().getId());
        dto.setAgentId(ticketAssignment.getAgent().getId());
        return dto;
    }

    @Override
    public List<TicketAssignmentResponseDTO> mapToDto(List<TicketAssignment> ticketAssignments) {
        return ticketAssignments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TicketAssignment mapToEntity(TicketAssignmentRequestDTO dto) {
        TicketAssignment ticketAssignment = new TicketAssignment();
        return ticketAssignment;
    }
}
