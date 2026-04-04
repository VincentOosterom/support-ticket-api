package nl.ticketsystem.dto.ticket_assignment;

import nl.ticketsystem.dto.history.TicketHistoryResponseDTO;
import nl.ticketsystem.model.TicketAssignment;

public class TicketAssignmentResponseDTO {

    private Long id;
    private String ticketId;
    private String agentId;

    public TicketAssignmentResponseDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
