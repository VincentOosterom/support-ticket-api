package nl.ticketsystem.repository;

import nl.ticketsystem.model.TicketAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAssignmentRepository extends JpaRepository<TicketAssignment, Long> {
}
