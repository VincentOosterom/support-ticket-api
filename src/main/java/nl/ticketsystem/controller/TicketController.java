package nl.ticketsystem.controller;

import nl.ticketsystem.dto.ticket.TicketRequestDTO;
import nl.ticketsystem.dto.ticket.TicketResponseDTO;
import nl.ticketsystem.dto.ticket.TicketStatusUpdateDTO;
import nl.ticketsystem.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(
            @RequestBody TicketRequestDTO dto,
            @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(dto, userId));
    }

    @PutMapping("/{id}/status")
        public ResponseEntity<TicketResponseDTO> updateTicketStatus (
                @PathVariable Long id,
                @RequestParam TicketStatusUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, dto.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
