package nl.ticketsystem.dto.ticket_assignment;

import nl.ticketsystem.dto.history.TicketHistoryResponseDTO;
import nl.ticketsystem.model.TicketAssignment;

public class TicketAssignmentResponseDTO {

    private Long id;
    private Long ticketId;
    private Long agentId;

    public TicketAssignmentResponseDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
}
