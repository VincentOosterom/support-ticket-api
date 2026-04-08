package nl.ticketsystem.controller;

import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentRequestDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentResponseDTO;
import nl.ticketsystem.model.TicketAssignment;
import nl.ticketsystem.service.TicketAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket-assignments")
public class TicketAssignmentController {

    private final TicketAssignmentService ticketAssignmentService;

    public TicketAssignmentController(TicketAssignmentService ticketAssignmentService) {
        this.ticketAssignmentService = ticketAssignmentService;
    }

    @GetMapping
    public ResponseEntity<List<TicketAssignmentResponseDTO>> getAllAssignments() {
        return ResponseEntity.ok(ticketAssignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketAssignmentResponseDTO> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketAssignmentService.getAssignmentById(id));
    }

    @PutMapping("/ticket/{ticketId}")
    public ResponseEntity<TicketAssignmentResponseDTO> updateAssignment(@PathVariable Long ticketId, @RequestParam Long newAgentId) {
        return ResponseEntity.ok(ticketAssignmentService.updateAssignment(ticketId, newAgentId));

    }

    @PostMapping
    public ResponseEntity<TicketAssignmentResponseDTO> createAssignment(@RequestBody TicketAssignmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketAssignmentService.createAssignment(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        ticketAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
