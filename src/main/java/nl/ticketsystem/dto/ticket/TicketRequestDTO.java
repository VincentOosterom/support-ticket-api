package nl.ticketsystem.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nl.ticketsystem.model.Priority;

public class TicketRequestDTO {

    @NotBlank(message = "Onderwerp mag niet leeg zijn")
    @Size(max = 255, message = "Onderwerp mag maximaal 255 tekens zijn")
    private String subject;

    @NotBlank(message = "Beschrijving mag niet leeg zijn")
    @Size(max = 1000, message = "Beschrijving mag maximaal 1000 tekens zijn")
    private String description;

    @NotNull(message = "Prioriteit mag niet leeg zijn")
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
