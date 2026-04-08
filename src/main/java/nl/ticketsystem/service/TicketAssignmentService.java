package nl.ticketsystem.service;

import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentRequestDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.TicketAssignmentMapper;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.model.TicketAssignment;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.TicketAssignmentRepository;
import nl.ticketsystem.repository.TicketRepository;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketAssignmentService {

    private final TicketAssignmentRepository ticketAssignmentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketAssignmentMapper ticketAssignmentMapper;

    public TicketAssignmentService(TicketAssignmentRepository ticketAssignmentRepository, TicketRepository ticketRepository, UserRepository userRepository, TicketAssignmentMapper ticketAssignmentMapper) {
        this.ticketAssignmentRepository = ticketAssignmentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.ticketAssignmentMapper = ticketAssignmentMapper;
    }

    public List<TicketAssignmentResponseDTO> getAllAssignments() {
        return ticketAssignmentMapper.mapToDto(ticketAssignmentRepository.findAll());
    }

    public TicketAssignmentResponseDTO getAssignmentById(Long id) {
        TicketAssignment ticketAssignment = ticketAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TicketAssignment niet gevonden"));
        return ticketAssignmentMapper.mapToDto(ticketAssignment);
    }

    public TicketAssignmentResponseDTO createAssignment(TicketAssignmentRequestDTO dto){
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));
        User agent = userRepository.findById(dto.getAgentId())
                .orElseThrow(() -> new ResourceNotFoundException("Agent niet gevonden"));
        TicketAssignment ticketAssignment = ticketAssignmentMapper.mapToEntity(dto);
        ticketAssignment.setTicket(ticket);
        ticketAssignment.setAgent(agent);
        TicketAssignment savedAssignment = ticketAssignmentRepository.save(ticketAssignment);
        return ticketAssignmentMapper.mapToDto(savedAssignment);
    }

    public List<TicketAssignmentResponseDTO>getAssignmentsByTicketId(Long ticketId){
        return ticketAssignmentMapper.mapToDto(ticketAssignmentRepository.findByTicketId(ticketId));
    }

    public void deleteAssignment(Long id) {
        ticketAssignmentRepository.deleteById(id);
    }


}
