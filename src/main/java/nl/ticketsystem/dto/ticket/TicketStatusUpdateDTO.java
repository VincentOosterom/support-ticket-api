package nl.ticketsystem.dto.ticket;

import nl.ticketsystem.model.TicketStatus;

public class TicketStatusUpdateDTO {

    private TicketStatus status;

    public TicketStatusUpdateDTO() {}
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
}

