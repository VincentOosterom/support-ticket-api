package nl.ticketsystem.service;

import nl.ticketsystem.dto.attachment.AttachmentRequestDTO;
import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.exception.FileTooLargeException;
import nl.ticketsystem.exception.InvalidFileTypeException;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.AttachmentMapper;
import nl.ticketsystem.model.Attachment;
import nl.ticketsystem.model.Ticket;
import nl.ticketsystem.repository.AttachmentRepository;
import nl.ticketsystem.repository.TicketRepository;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {
    private final TicketRepository ticketRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/png", "image/jepg", "application/pdf"
    );

    private static final long MAX_SIZE = 5 * 1024 * 1024;


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
                .orElseThrow(() -> new ResourceNotFoundException("Attachment niet gevonden"));
        return attachmentMapper.mapToDto(attachment);
    }

    public AttachmentResponseDTO uploadFile(MultipartFile file, Long ticketId) throws IOException {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new InvalidFileTypeException("Bestandstype niet toegestaan");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new FileTooLargeException("Bestand mag maximaal 5MB zijn");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket niet gevonden"));

        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String unqiueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(unqiueFileName);
        Files.copy(file.getInputStream(), filePath);

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setFilePath(filePath.toString());
        attachment.setUploadDate(LocalDateTime.now());
        attachment.setTicket(ticket);

        Attachment saved = attachmentRepository.save(attachment);
        return attachmentMapper.mapToDto(saved);
    }

    public List<AttachmentResponseDTO> getAttachmentsByTicketId(Long ticketId) {
        return attachmentMapper.mapToDto(attachmentRepository.findByTicketId(ticketId));
    }

    public void deleteAttachment(Long id) {
        attachmentRepository.deleteById(id);
    }
}
