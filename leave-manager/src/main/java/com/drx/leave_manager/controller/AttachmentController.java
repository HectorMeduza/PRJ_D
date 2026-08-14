package com.drx.leave_manager.controller;

import com.drx.leave_manager.model.Attachment;
import com.drx.leave_manager.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/attachments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseEntity<Attachment> uploadAttachment(
            @RequestParam("leaveRequestId") Integer leaveRequestId,
            @RequestParam("file") MultipartFile file) {
        try {
            Attachment savedAttachment = attachmentService.uploadFile(leaveRequestId, file);
            return ResponseEntity.ok(savedAttachment);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la salvarea fișierului: " + e.getMessage());
        }
    }
}