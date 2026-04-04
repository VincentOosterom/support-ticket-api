package nl.ticketsystem.service;

import nl.ticketsystem.dto.attachment.AttachmentRequestDTO;
import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.mapper.AttachmentMapper;
import nl.ticketsystem.model.Attachment;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.repository.AttachmentRepository;
import nl.ticketsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {

    private final TicketRepository ticketRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    public AttachmentService(TicketRepository ticketRepository, AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper) {
        this.ticketRepository = ticketRepository;
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    public List<AttachmentResponseDTO> getAllAttachment() {
        return attachmentMapper.mapToDto(attachmentRepository.findAll());
    }

    public AttachmentResponseDTO getAttachmentById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment niet gevonden"));
        return attachmentMapper.mapToDto(attachment);
    }

    public AttachmentResponseDTO createAttachment(AttachmentRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket niet gevonden"));
        Attachment attachment = attachmentMapper.mapToEntity(dto);
        attachment.setTicket(ticket);
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return attachmentMapper.mapToDto(savedAttachment);
    }

    public void deleteAttachment(Long id) {
        attachmentRepository.deleteById(id);
    }
}
