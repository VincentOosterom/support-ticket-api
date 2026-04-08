package nl.ticketsystem.controller;

import nl.ticketsystem.dto.attachment.AttachmentRequestDTO;
import nl.ticketsystem.dto.attachment.AttachmentResponseDTO;
import nl.ticketsystem.service.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{attachments}")
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

    @PostMapping
    public ResponseEntity<AttachmentResponseDTO> createAttachment(@RequestBody AttachmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachmentService.createAttachment(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteAttachment(@PathVariable Long id){
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}