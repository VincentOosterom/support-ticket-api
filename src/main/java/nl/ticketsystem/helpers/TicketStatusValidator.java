package nl.ticketsystem.helpers;

import nl.ticketsystem.exception.InvalidStatusTransitionException;
import nl.ticketsystem.model.TicketStatus;
import org.springframework.stereotype.Component;

@Component
public class TicketStatusValidator {

    public void validate(TicketStatus current, TicketStatus newStatus) {
        boolean valid = switch (current) {
            case OPEN -> newStatus == TicketStatus.IN_PROGRESS;
            case IN_PROGRESS -> newStatus == TicketStatus.RESOLVED;
            case RESOLVED -> newStatus == TicketStatus.CLOSED;
            case CLOSED -> false;
        };

        if (!valid) {
            throw new InvalidStatusTransitionException("Ongeldige statusovergang van " + current + " naar " + newStatus);
        }
    }
}
