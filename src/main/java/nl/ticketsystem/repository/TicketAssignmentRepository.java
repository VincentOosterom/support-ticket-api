package nl.ticketsystem.repository;

import nl.ticketsystem.model.TicketAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketAssignmentRepository extends JpaRepository<TicketAssignment, Long> {
    List<TicketAssignment> findByTicketId(Long ticketId);
    Optional<TicketAssignment> findFirstByTicketId(Long ticketId);
}
