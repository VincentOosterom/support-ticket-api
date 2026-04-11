package nl.ticketsystem.controller;

import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.dto.comment.CommentResponseDTO;
import nl.ticketsystem.dto.history.TicketHistoryResponseDTO;
import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.dto.ticket.TicketStatusUpdateDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentResponseDTO;
import nl.ticketsystem.model.Priority;
import nl.ticketsystem.model.TicketStatus;
import nl.ticketsystem.model.User;
import nl.ticketsystem.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final CommentService commentService;
    private final AttachmentService attachmentService;
    private final TicketHistoryService ticketHistoryService;
    private final TicketAssignmentService ticketAssignmentService;

    public TicketController(TicketService ticketService, CommentService commentService, AttachmentService attachmentService, TicketHistoryService ticketHistoryService, TicketAssignmentService ticketAssignmentService) {
        this.ticketService = ticketService;
        this.commentService = commentService;
        this.attachmentService = attachmentService;
        this.ticketHistoryService = ticketHistoryService;
        this.ticketAssignmentService = ticketAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTicketId(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByTicketId(id));
    }

    @GetMapping("/{id}/attachments")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByTicketId(@PathVariable Long id) {
        return ResponseEntity.ok(attachmentService.getAttachmentsByTicketId(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<TicketHistoryResponseDTO>> getHistoryByTicketId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketHistoryService.getTicketHistoryByTicketId(id));
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<TicketAssignmentResponseDTO>> getAssignmentsByTicketId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketAssignmentService.getAssignmentsByTicketId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketByStatus(@PathVariable TicketStatus status) {
        return ResponseEntity.ok(ticketService.getTicketByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketByPriority(@PathVariable Priority priority) {
        return ResponseEntity.ok(ticketService.getTicketByPriority(priority));
    }

    @GetMapping("/users/{id}/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getTicketByUser(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketByUser(id));
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(
            @RequestBody TicketRequestDTO dto,
            @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(dto, userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(
            @PathVariable Long id,
            @RequestBody TicketStatusUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, dto.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
