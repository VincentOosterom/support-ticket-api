package nl.ticketsystem.dto.ticket;

import nl.ticketsystem.model.Priority;

public class TicketRequestDTO {

    private String subject;
    private String description;
    private Priority priority;

    public TicketRequestDTO() {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
