package nl.ticketsystem.repository;

import nl.ticketsystem.model.Priority;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import nl.ticketsystem.model.User;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByPriority(Priority priority);
    List<Ticket> findByUser(User user);
}
