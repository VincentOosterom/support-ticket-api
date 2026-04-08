package nl.ticketsystem.service;

import nl.ticketsystem.dto.comment.CommentRequestDTO;
import nl.ticketsystem.dto.comment.CommentResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.CommentMapper;
import nl.ticketsystem.model.Comment;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.CommentRepository;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TicketRepository ticketRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentResponseDTO>getAllComments() {
        return commentMapper.mapToDto(commentRepository.findAll());
    }

    public CommentResponseDTO getCommentById(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment niet gevonden"));
        return commentMapper.mapToDto(comment);
    }

    public CommentResponseDTO createComment(CommentRequestDTO dto){
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));
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
