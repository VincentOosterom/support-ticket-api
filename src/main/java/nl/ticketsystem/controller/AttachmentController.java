package nl.ticketsystem.controller;

import org.springframework.core.io.Resource;
import nl.ticketsystem.dto.attachment.AttachmentRequestDTO;
import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.service.AttachmentService;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping
    public ResponseEntity<List<AttachmentResponseDTO>> getAllAttachment() {
        return ResponseEntity.ok(attachmentService.getAllAttachment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttachmentResponseDTO> getAttachmentById(@PathVariable Long id) {
        return ResponseEntity.ok(attachmentService.getAttachmentById(id));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ticketId") Long ticketId) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachmentService.uploadFile(file, ticketId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        AttachmentResponseDTO dto = attachmentService.getAttachmentById(id);
        Path filePath = Paths.get(dto.getFilePath());
        org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteAttachment(@PathVariable Long id){
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}