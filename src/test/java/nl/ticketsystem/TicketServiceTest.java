package nl.ticketsystem;

import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.exception.InvalidStatusTransitionException;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.TicketMapper;
import nl.ticketsystem.model.Priority;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import nl.ticketsystem.repository.TicketHistoryRepository;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import nl.ticketsystem.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void getTicketById_ticketNietGevonden_gooitExecption() {

        Long id = 1L;
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicketById(id));
    }

    @Test
    void getTicketById_ticketGevonden_geeftTicketResponseDTO() {

        Long id = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setSubject("Test subject");

        TicketResponseDTO responseDTO = new TicketResponseDTO();
        responseDTO.setId(id);

        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        when(ticketMapper.mapToDto(ticket)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.getTicketById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void deleteTicket_statusOpen() {

        Long id = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        assertThrows(InvalidStatusTransitionException.class, () -> ticketService.deleteTicket(id));
    }

    @Test
    void deleteTicket_statusClosed() {

        Long id = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(TicketStatus.CLOSED);

        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(id);

        verify(ticketRepository).deleteById(id);
    }

    @Test
    void getAverageDaysToClose_geenClosedTickets_geeftNul() {

        when(ticketRepository.findByStatus(TicketStatus.CLOSED)).thenReturn(List.of());

        double result = ticketService.getAverageDaysToClose();

        assertEquals(0.0, result);
    }

    @Test
    void getAverageDaysToClose_welClosedTickets() {

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setCreatedAt(LocalDateTime.now().minusDays(5));
        ticket.setClosedAt(LocalDateTime.now());

        when(ticketRepository.findByStatus(TicketStatus.CLOSED)).thenReturn(List.of(ticket));

        double result = ticketService.getAverageDaysToClose();

        assertEquals(5.0, result);
    }

    @Test
    void getTicketByPriority_priorityHigh_geeftLijst() {

        Ticket ticket = new Ticket();
        ticket.setPriority(Priority.HIGH);

        TicketResponseDTO responseDTO = new TicketResponseDTO();

        when(ticketRepository.findByPriority(Priority.HIGH)).thenReturn(List.of(ticket));
        when(ticketMapper.mapToDto(List.of(ticket))).thenReturn(List.of(responseDTO));

        List<TicketResponseDTO> result = ticketService.getTicketByPriority(Priority.HIGH);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void deleteTicket_ticketNietGevonden_gooitException() {

        Long id = 1L;
        when(ticketRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.deleteTicket(id));
    }

    @Test
    void getAverageDaysToClose_closedAtIsNull_wordtOvergeslagen() {

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setCreatedAt(LocalDateTime.now().minusDays(5));

        when(ticketRepository.findByStatus(TicketStatus.CLOSED)).thenReturn(List.of(ticket));

        double result = ticketService.getAverageDaysToClose();

        assertEquals(0.0, result);
    }

    @Test
    void getTicketByStatus_closedAlsNietAdmin_gooitException() {

        when(authentication.getAuthorities()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> ticketService.getTicketByStatus(TicketStatus.CLOSED, authentication));

    }
}
