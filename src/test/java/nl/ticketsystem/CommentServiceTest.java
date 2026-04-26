package nl.ticketsystem;

import nl.ticketsystem.dto.comment.CommentRequestDTO;
import nl.ticketsystem.dto.comment.CommentResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.CommentMapper;
import nl.ticketsystem.model.Comment;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.CommentRepository;
import nl.ticketsystem.repository.TicketAssignmentRepository;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import nl.ticketsystem.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketAssignmentRepository ticketAssignmentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private CommentService commentService;


    @Test
    void getCommentById_commentNietGevonden_gooitException() {

        Long id = 1L;

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(id));
    }

    @Test
    void getCommentById_commentWelGevonden() {

        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);
        comment.setMessage("Test comment");

        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setId(id);

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.mapToDto(comment)).thenReturn(responseDTO);

        CommentResponseDTO result = commentService.getCommentById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void createComment_AgentNotAssignment() {

        String keycloakId = "test-keycloak-id";
        Long ticketId = 1L;

        User agent = new User();
        agent.setKeycloakId(keycloakId);

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setTicketId(ticketId);

        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(keycloakId);
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(agent));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_AGENT"))).when(authentication).getAuthorities();
        when(ticketAssignmentRepository.existsByTicketAndAgent(ticket, agent)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> commentService.createComment(dto, authentication));
    }

    @Test
    void getAllComments_geeftLijst() {
        // Arrange
        when(commentRepository.findAll()).thenReturn(List.of());
        when(commentMapper.mapToDto(List.of())).thenReturn(List.of());

        // Act
        List<CommentResponseDTO> result = commentService.getAllComments();

        // Assert
        assertNotNull(result);
    }

    @Test
    void deleteComment() {
        Long id = 22L;

        commentService.deleteComment(id);

        verify(commentRepository).deleteById(id);
    }

}