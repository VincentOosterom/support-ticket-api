package nl.ticketsystem;

import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.exception.InvalidStatusTransitionException;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.helpers.TicketStatusValidator;
import nl.ticketsystem.mapper.TicketMapper;
import nl.ticketsystem.model.Priority;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketStatus;
import nl.ticketsystem.model.User;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketStatusValidator ticketStatusValidator;

    @Mock
    private TicketHistoryRepository ticketHistoryRepository;

    @Mock
    private Jwt jwt;

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
    void createTicket() {

        String keycloakId = "test-keycloak-id";

        User customer = new User();
        customer.setKeycloakId(keycloakId);

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setSubject("Test Ticket");
        dto.setPriority(Priority.HIGH);

        Ticket ticket = new Ticket();
        Ticket savedTicket = new Ticket();

        savedTicket.setId(1l);

        TicketResponseDTO responseDTO = new TicketResponseDTO();
        responseDTO.setId(1L);

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(customer));
        when(ticketMapper.mapToEntity(dto)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);
        when(ticketMapper.mapToDto(savedTicket)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.createTicket(dto, keycloakId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
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

    @Test
    void createTicket_userNietGevonden_gooitException() {

        String keycloakId = "test-keycloak-id";
        TicketRequestDTO dto = new TicketRequestDTO();

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.createTicket(dto, keycloakId));
    }

    @Test
    void getAllTickets_alsAdmin_geeftAlleTickets() {

        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn("test-keycloak-id");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(authentication).getAuthorities();
        when(ticketRepository.findByStatusNot(TicketStatus.CLOSED))
                .thenReturn(List.of());
        when(ticketMapper.mapToDto(List.of()))
                .thenReturn(List.of());

        List<TicketResponseDTO> result = ticketService.getAllTickets(authentication);

        assertNotNull(result);
    }

    @Test
    void getAllTickets_alsCustomer_geeftEigenTickets() {

        String keycloakId = "test-keycloak-id";
        User customer = new User();
        customer.setKeycloakId(keycloakId);

        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(keycloakId);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .when(authentication).getAuthorities();
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(customer));
        when(ticketRepository.findByUserAndStatusNot(customer, TicketStatus.CLOSED))
                .thenReturn(List.of());
        when(ticketMapper.mapToDto(List.of())).thenReturn(List.of());

        List<TicketResponseDTO> result = ticketService.getAllTickets(authentication);

        assertNotNull(result);
    }

    @Test
    void getTicketByUser() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findByUser(user)).thenReturn(List.of());
        when(ticketMapper.mapToDto(List.of())).thenReturn(List.of());

        List<TicketResponseDTO> result = ticketService.getTicketByUser(userId);

        assertNotNull(result);
    }

    @Test
    void updateTicketStatus_succesvol() {

        String keycloakId = "test-keycloak-id";
        Long id = 1L;

        User user = new User();
        user.setKeycloakId(keycloakId);

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(TicketStatus.OPEN);

        TicketResponseDTO responseDTO = new TicketResponseDTO();
        responseDTO.setId(id);

        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(keycloakId);
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.mapToDto(ticket)).thenReturn(responseDTO);

        TicketResponseDTO result = ticketService.updateTicketStatus(id, TicketStatus.IN_PROGRESS, authentication);

        assertNotNull(result);
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
    }

    @Test
    void updateTicketStatus_naarClosed_setsClosedAt() {
        // Arrange
        String keycloakId = "test-keycloak-id";
        Long id = 1L;

        User user = new User();
        user.setKeycloakId(keycloakId);

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        TicketResponseDTO responseDTO = new TicketResponseDTO();

        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(keycloakId);
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user));
        when(ticketRepository.findById(id)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.mapToDto(ticket)).thenReturn(responseDTO);

        // Act
        ticketService.updateTicketStatus(id, TicketStatus.CLOSED, authentication);

        // Assert
        assertNotNull(ticket.getClosedAt());
    }

    @Test
    void getTicketByStatus_alsAdmin_geeftTickets() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(authentication).getAuthorities();
        when(ticketRepository.findByStatus(TicketStatus.OPEN)).thenReturn(List.of());
        when(ticketMapper.mapToDto(List.of())).thenReturn(List.of());

        List<TicketResponseDTO> result = ticketService.getTicketByStatus(TicketStatus.OPEN, authentication);

        assertNotNull(result);
    }

}
