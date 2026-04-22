package nl.ticketsystem;

import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.exception.InvalidStatusTransitionException;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.TicketMapper;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketHistoryRepository ticketHistoryRepository;

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
    void deleteTicket_statusNietClosed() {

        Long id = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(TicketStatus.OPEN);

        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));

        assertThrows(InvalidStatusTransitionException.class, () -> ticketService.deleteTicket(id));
    }
}
