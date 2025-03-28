package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.UriEncoder;
import uz.academy.exam.Exam.exceptions.CustomNotFoundException;
import uz.academy.exam.Exam.model.entity.Attachment;
import uz.academy.exam.Exam.repository.AttachmentRepository;
import uz.academy.exam.Exam.service.base.AttachmentService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:file.properties")
public class IAttachmentService implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private static final Logger log = LoggerFactory.getLogger(IAttachmentService.class);


    @Value("${file.base.url}")
    private String URL_TO_SAVE_FILE;

    @Value("${load-image.url}")
    private String LOAD_IMG_URL;

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Long upload(MultipartFile img) {
        Attachment attachment = uploadAttachmentToDb(img);
        uploadToStorage(img, attachment);
        return attachment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> uploads(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(this::upload).toList();
    }

//    @Transactional(rollbackFor = Exception.class)
//    public ApiResponse deleteById(Long attachmentId) {
//        Attachment attachment = getById(attachmentId);
//
//        try {
//            deleteFromStorage(attachment);
//            attachmentRepository.delete(attachment);
//        } catch (IOException e) {
//            throw new CustomBadRequestException("Failed to delete attachment");
//        }
//
//        return ApiResponse.builder()
//                .message("Attachment deleted successfully")
//                .build();
//    }

    @Override
    public ResponseEntity<FileUrlResource> getImage(Long attachmentId) {
        Attachment attachment = getById(attachmentId);

        try {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; fileName=" +
                                    UriEncoder.encode(attachment.getFileName()))
                    .contentType(MediaType.parseMediaType(attachment.getFileType()))
                    .body(
                            new FileUrlResource(
                                    attachment.getFileUrl())
                    );
        } catch (MalformedURLException ignored) {
            log.error("Attachment not found with id: {}", attachmentId);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public Attachment getById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(
                        () -> new CustomNotFoundException("Attachment not found with id: " + attachmentId)
                );
    }

//    @Override
//    public AttachmentResponse toResponse(Attachment attachment) {
//        if (attachment == null)
//            return null;
//
//        return AttachmentResponse.builder()
//                .id(attachment.getId())
//                .url(LOAD_IMG_URL + attachment.getId())
//                .build();
//    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long attachmentId) {
        Attachment attachment = getById(attachmentId);

        if (attachment == null)
            throw new IllegalArgumentException("Attachment with ID " + attachmentId + " not found");

        byte[] fileContent = fetchFileContent(attachment);

        if (fileContent.length == 0)
            throw new IllegalArgumentException("File content is empty for attachment ID " + attachmentId);

        HttpHeaders headers = getHttpHeaders(attachment);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @Override
    public List<Attachment> findAllById(List<Long> attachmentIds) {
        return attachmentRepository.findAllById(attachmentIds);
    }

    @Override
    public String getImageUrlById(long imageId) {
        return LOAD_IMG_URL + imageId;
    }

//    @Override
//    public void deleteAll(List<Attachment> images) {
//        if (images == null || images.isEmpty())
//            return;
//
//        attachmentRepository.deleteAll(images);
//    }

    private HttpHeaders getHttpHeaders(Attachment attachment) {
        String contentType = attachment.getFileType();

        if (contentType == null || contentType.isEmpty())
            contentType = "application/octet-stream";

        String fileName = attachment.getFileName();

        if (fileName == null || fileName.isEmpty())
            fileName = "downloaded-file";


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        return headers;
    }

    @SneakyThrows
    private byte[] fetchFileContent(Attachment attachment) {
        return Files.readAllBytes(Paths.get(attachment.getFileUrl()));
    }

    private void deleteFromStorage(Attachment attachment) throws IOException {
        Path path = Paths.get(attachment.getFileUrl());
        Files.delete(path);
    }

    private void uploadToStorage(MultipartFile img, Attachment attachment) throws IOException {
        Path path = Paths.get(attachment.getFileUrl());
        Files.write(path, img.getBytes());
    }

    private Attachment uploadAttachmentToDb(MultipartFile file) {
        Attachment attachment = new Attachment();
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        attachment.setFileName(file.getOriginalFilename());
        attachment.setExtension(extension);
        attachment.setFileType(file.getContentType());
        attachment.setFileUrl(generateFileName(attachment));

        return attachmentRepository.saveAndFlush(attachment);
    }

    private String generateFileName(Attachment attachment) {
        return URL_TO_SAVE_FILE + "/" + UUID.randomUUID() + "-" + System.currentTimeMillis() + "." + attachment.getExtension();
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0)
            return fileName.substring(index + 1);

        return null;
    }
}