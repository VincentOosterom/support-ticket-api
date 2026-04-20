package nl.ticketsystem.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequestDTO {

    @NotBlank(message = "Bericht mag niet leeg zijn")
    @Size(max = 1000, message = "Bericht mag maximaal 1000 tekens zijn")
    private String message;

    private Long ticketId;
    private Long userId;

    public CommentRequestDTO() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
