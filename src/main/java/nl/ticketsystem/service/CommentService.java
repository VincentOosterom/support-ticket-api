package nl.ticketsystem.service;

import nl.ticketsystem.dto.comment.CommentRequestDTO;
import nl.ticketsystem.dto.comment.CommentResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.CommentMapper;
import nl.ticketsystem.model.Comment;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketAssignment;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final CommentMapper commentMapper;
    private final TicketAssignmentRepository ticketAssignmentRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TicketRepository ticketRepository, CommentMapper commentMapper, TicketAssignmentRepository ticketAssignmentRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.commentMapper = commentMapper;
        this.ticketAssignmentRepository = ticketAssignmentRepository;
    }

    public List<CommentResponseDTO>getAllComments() {
        return commentMapper.mapToDto(commentRepository.findAll());
    }

    public CommentResponseDTO getCommentById(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment niet gevonden"));
        return commentMapper.mapToDto(comment);
    }

    public CommentResponseDTO createComment(CommentRequestDTO dto, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();

        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));

        boolean isAgent = authentication.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_AGENT"));

        if (isAgent && !ticketAssignmentRepository.existsByTicketAndAgent(ticket, user)) {
            throw new RuntimeException("Agent is niet toegewezen aan dit ticket");
        }

        Comment comment = commentMapper.mapToEntity(dto);
        comment.setTicket(ticket);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.mapToDto(savedComment);
    }

    public List<CommentResponseDTO>getCommentsByTicketId(Long ticketId){
        return commentMapper.mapToDto(commentRepository.findByTicketId(ticketId));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
