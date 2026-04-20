package nl.ticketsystem.dto.history;

import nl.ticketsystem.model.TicketStatus;

public class TicketHistoryRequestDTO {


    private TicketStatus newStatus;
    private TicketStatus oldStatus;
    private Long ticketId;
    private Long changeById;

    public TicketHistoryRequestDTO() {

    }

    public TicketStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(TicketStatus newStatus) {
        this.newStatus = newStatus;
    }

    public TicketStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(TicketStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getChangeById() {
        return changeById;
    }

    public void setChangeById(Long changeById) {
        this.changeById = changeById;
    }
}
