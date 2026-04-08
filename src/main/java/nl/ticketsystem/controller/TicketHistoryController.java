package nl.ticketsystem.controller;

import nl.ticketsystem.dto.history.TicketHistoryRequestDTO;
import nl.ticketsystem.dto.history.TicketHistoryResponseDTO;
import nl.ticketsystem.service.TicketHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket-history")
public class TicketHistoryController {

    private final TicketHistoryService ticketHistoryService;

    public TicketHistoryController(TicketHistoryService ticketHistoryService) {
        this.ticketHistoryService = ticketHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<TicketHistoryResponseDTO>> getAllTicketHistories() {
        return ResponseEntity.ok(ticketHistoryService.getAllTicketHistories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketHistoryResponseDTO> getTicketHistoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketHistoryService.getTicketHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<TicketHistoryResponseDTO> createTicketHistory(@RequestBody TicketHistoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketHistoryService.createTicketHistory(dto));
    }
}
