package nl.ticketsystem.dto.ticket_assignment;

import nl.ticketsystem.dto.history.TicketHistoryRequestDTO;
import nl.ticketsystem.model.TicketAssignment;

public class TicketAssignmentRequestDTO {

    private Long ticketId;
    private Long agentId;

    public TicketAssignmentRequestDTO() {

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
