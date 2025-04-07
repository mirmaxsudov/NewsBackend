package uz.academy.exam.Exam.service.base;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.academy.exam.Exam.model.entity.attachment.Attachment;

import java.util.List;

public interface AttachmentService {
    Long upload(MultipartFile file);

    List<Long> uploads(List<MultipartFile> multipartFiles);

    ResponseEntity<FileUrlResource> getAttachment(Long attachmentId);

    Attachment getById(Long attachmentId);

    ResponseEntity<byte[]> downloadFile(Long attachmentId);

    List<Attachment> findAllById(List<Long> attachmentIds);

    String getAttachmentUrlById(long attachmentId);

    void deleteById(Long attachmentId);

    ResponseEntity<ResourceRegion> streamVideo(Long attachmentId, HttpHeaders headers);
}