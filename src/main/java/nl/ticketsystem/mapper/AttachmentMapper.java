package nl.ticketsystem.mapper;

import nl.ticketsystem.dto.attachment.AttachmentRequestDTO;
import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.model.Attachment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttachmentMapper implements DTOMapper<AttachmentResponseDTO, AttachmentRequestDTO, Attachment> {

    @Override
    public AttachmentResponseDTO mapToDto(Attachment attachment) {
        AttachmentResponseDTO dto = new AttachmentResponseDTO();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFileType(attachment.getFileType());
        dto.setFilePath(attachment.getFilePath());
        dto.setFileSize(attachment.getFileSize());
        dto.setUploadDate(attachment.getUploadDate());
        dto.setTicketId(attachment.getTicket().getId());
        return dto;
    }

    @Override
    public List<AttachmentResponseDTO> mapToDto(List<Attachment> attachments) {
        return attachments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Attachment mapToEntity(AttachmentRequestDTO dto) {
        Attachment attachment = new Attachment();
        attachment.setFileName(dto.getFileName());
        attachment.setFileType(dto.getFileType());
        attachment.setFileSize(dto.getFileSize());
        attachment.setUploadDate(LocalDateTime.now());
        return attachment;
    }
}