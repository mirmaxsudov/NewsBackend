package uz.academy.exam.Exam.service.base;

import org.springframework.core.io.FileUrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import uz.academy.exam.Exam.model.entity.Attachment;

import java.util.List;

public interface AttachmentService {
    Long upload(MultipartFile img);

    List<Long> uploads(List<MultipartFile> multipartFiles);

//    ApiResponse deleteById(Long attachmentId);

    ResponseEntity<FileUrlResource> getImage(Long imgId);

    Attachment getById(Long attachmentId);

//    AttachmentResponse toResponse(Attachment attachment);

    ResponseEntity<byte[]> downloadFile(Long attachmentId);

    List<Attachment> findAllById(List<Long> attachmentIds);

    String getImageUrlById(long imageId);
}