package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.academy.exam.Exam.model.response.ApiResponse;
import uz.academy.exam.Exam.service.base.AttachmentService;
import uz.academy.exam.Exam.util.ApiUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/stream/{attachmentId}")
    public ResponseEntity<ResourceRegion> streamVideo(
            @PathVariable("attachmentId") Long attachmentId,
            @RequestHeader HttpHeaders headers
    ) {
        return attachmentService.streamVideo(attachmentId, headers);
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<FileUrlResource> getAttachment(
            @PathVariable("attachmentId") Long attachmentId
    ) {
        return attachmentService.getAttachment(attachmentId);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<ApiResponse<Long>> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachmentService.upload(file));
    }

    @PostMapping(value = "/uploads", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<ApiResponse<List<Long>>> uploads(@RequestParam("files") List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(attachmentService.uploads(multipartFiles));
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<byte[]> download(@PathVariable("attachmentId") Long attachmentId) {
        return attachmentService.downloadFile(attachmentId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{attachmentId}")
    public void delete(@PathVariable("attachmentId") Long attachmentId) {
        attachmentService.deleteById(attachmentId);
    }
}