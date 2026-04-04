package nl.ticketsystem.mapper;

import nl.ticketsystem.dto.comment.CommentRequestDTO;
import nl.ticketsystem.dto.comment.CommentResponseDTO;
import nl.ticketsystem.model.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper implements DTOMapper<CommentResponseDTO, CommentRequestDTO, Comment> {

    @Override
    public CommentResponseDTO mapToDto(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setTicketId(comment.getTicket().getId());
        dto.setUserId(comment.getUser().getId());
        return dto;
    }

    @Override
    public List<CommentResponseDTO> mapToDto(List<Comment> comments) {
        return comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment mapToEntity(CommentRequestDTO dto) {
        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }
}
