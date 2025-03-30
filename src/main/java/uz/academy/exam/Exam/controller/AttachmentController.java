package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.academy.exam.Exam.service.base.AttachmentService;
import uz.academy.exam.Exam.util.ApiUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @GetMapping("/{imgId}")
    public ResponseEntity<FileUrlResource> get(
            @PathVariable("imgId") Long imgId
    ) {
        return attachmentService.getImage(imgId);
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Long> upload(@RequestParam("img") MultipartFile img) {
        return ResponseEntity.ok(attachmentService.upload(img));
    }

    @PostMapping(value = "/uploads", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<List<Long>> uploads(@RequestParam("imgs") List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(attachmentService.uploads(multipartFiles));
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<byte[]> download(@PathVariable("attachmentId") Long attachmentId) {
        return attachmentService.downloadFile(attachmentId);
    }

//    @DeleteMapping("/{attachmentId}")
////    @PreAuthorize("hasAnyRole('ADMIN', 'GUIDE', 'TOURIST')")
//    public ResponseEntity<ApiResponse> delete(@PathVariable("attachmentId") Long attachmentId) {
//        return ResponseEntity.ok(attachmentService.deleteById(attachmentId));
//    }
}