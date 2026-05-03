package nl.ticketsystem.service;


import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentRequestDTO;
import nl.ticketsystem.dto.ticket_assignment.TicketAssignmentResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.TicketAssignmentMapper;
import nl.ticketsystem.model.*;
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

    public TicketAssignmentResponseDTO createAssignment(TicketAssignmentRequestDTO dto, String keycloakId, boolean isAdmin) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));
        User agent = userRepository.findById(dto.getAgentId())
                .orElseThrow(() -> new ResourceNotFoundException("Agent niet gevonden"));

        if (!isAdmin && !agent.getKeycloakId().equals(keycloakId)) {
            throw new RuntimeException("Een agent kan alleen zichzelf toewijzen");
        }

        if (ticketAssignmentRepository.existsByTicket(ticket)) {
            throw new RuntimeException("Deze ticket is al toegewezen");
        }

        TicketAssignment ticketAssignment = ticketAssignmentMapper.mapToEntity(dto);
        ticketAssignment.setTicket(ticket);
        ticketAssignment.setAgent(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticketRepository.save(ticket);
        TicketAssignment savedAssignment = ticketAssignmentRepository.save(ticketAssignment);
        return ticketAssignmentMapper.mapToDto(savedAssignment);
    }

    public List<TicketAssignmentResponseDTO> getAssignmentsByTicketId(Long ticketId) {
        return ticketAssignmentMapper.mapToDto(ticketAssignmentRepository.findByTicketId(ticketId));
    }

    public List<TicketAssignmentResponseDTO> getMyAssignments(String keycloakId) {
        User agent = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent niet gevonden"));
        return ticketAssignmentMapper.mapToDto(ticketAssignmentRepository.findByAgentAndTicket_StatusNot(agent,TicketStatus.CLOSED));
    }

    public TicketAssignmentResponseDTO updateAssignment(Long ticketId, Long newAgentId) {
        TicketAssignment assignment = ticketAssignmentRepository.findFirstByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Toewijzing niet gevonden voor ticket:" + ticketId));

        User newAgent = userRepository.findById(newAgentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent met dit id" + newAgentId + "niet gevonden"));

        assignment.setAgent(newAgent);
        TicketAssignment savedAssignment = ticketAssignmentRepository.save(assignment);
        return ticketAssignmentMapper.mapToDto(savedAssignment);
    }

    public void deleteAssignment(Long id) {
        TicketAssignment assignment = ticketAssignmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Assignment met" + id + "niet gevonden"));

        Ticket ticket = assignment.getTicket();
        ticket.setStatus(TicketStatus.OPEN);
        ticketRepository.save(ticket);


        ticketAssignmentRepository.deleteById(id);
    }


}
