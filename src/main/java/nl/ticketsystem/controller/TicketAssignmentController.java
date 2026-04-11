package nl.ticketsystem.controller;

import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentRequestDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentResponseDTO;
import nl.ticketsystem.model.TicketAssignment;
import nl.ticketsystem.service.TicketAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @GetMapping("/agent-id/{id}")
    public ResponseEntity<List<TicketAssignmentResponseDTO>> getTicketByAgent(@PathVariable Long id) {
        return ResponseEntity.ok(ticketAssignmentService.getTicketByAgent(id));

    }

    @PutMapping("/ticket/{ticketId}")
    public ResponseEntity<TicketAssignmentResponseDTO> updateAssignment(@PathVariable Long ticketId, @RequestParam Long newAgentId) {
        return ResponseEntity.ok(ticketAssignmentService.updateAssignment(ticketId, newAgentId));

    }

    @PostMapping
    public ResponseEntity<TicketAssignmentResponseDTO> createAssignment(
            @RequestBody TicketAssignmentRequestDTO dto,
            Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketAssignmentService.createAssignment(dto, keycloakId, isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        ticketAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
