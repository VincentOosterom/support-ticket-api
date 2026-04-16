package nl.ticketsystem.repository;

import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketAssignment;
import nl.ticketsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketAssignmentRepository extends JpaRepository<TicketAssignment, Long> {
    List<TicketAssignment> findByTicketId(Long ticketId);

    Optional<TicketAssignment> findFirstByTicketId(Long ticketId);

    List<TicketAssignment> findByAgent(User user);

    boolean existsByTicket(Ticket ticket);

    boolean existsByTicketAndAgent(Ticket ticket, User agent);

}
