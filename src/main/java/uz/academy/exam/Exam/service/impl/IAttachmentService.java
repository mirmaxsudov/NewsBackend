package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.yaml.snakeyaml.util.UriEncoder;
import uz.academy.exam.Exam.exceptions.CustomConflictException;
import uz.academy.exam.Exam.exceptions.CustomNotFoundException;
import uz.academy.exam.Exam.model.entity.attachment.Attachment;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.attachment.VideoAttachment;
import uz.academy.exam.Exam.model.enums.AttachmentType;
import uz.academy.exam.Exam.model.response.ApiResponse;
import uz.academy.exam.Exam.repository.AttachmentRepository;
import uz.academy.exam.Exam.service.base.AttachmentService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public ApiResponse<Long> upload(MultipartFile file) {
        Attachment attachment = uploadAttachmentToDb(file);
        uploadToStorage(file, attachment);
        return new ApiResponse<Long>(true, "Attachment uploaded successfully with id: " + attachment.getId(), attachment.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<List<Long>> uploads(List<MultipartFile> multipartFiles) {
        return new ApiResponse<>(
                true,
                "Successfully uploaded attachments",
                multipartFiles.stream().map(this::upload).toList().stream()
                        .map(ApiResponse::getData).toList()
        );
    }

    @Override
    public ResponseEntity<FileUrlResource> getAttachment(Long attachmentId) {
        Attachment attachment = getById(attachmentId);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; fileName=" + UriEncoder.encode(attachment.getFileName()))
                    .contentType(MediaType.parseMediaType(attachment.getFileType()))
                    .body(new FileUrlResource(attachment.getFileUrl()));
        } catch (MalformedURLException ignored) {
            log.error("Attachment not found with id: {}", attachmentId);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public Attachment getById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new CustomNotFoundException("Attachment not found with id: " + attachmentId));
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(Long attachmentId) {
        Attachment attachment = getById(attachmentId);

        byte[] fileContent = fetchFileContent(attachment);

        if (fileContent.length == 0) {
            throw new IllegalArgumentException("File content is empty for attachment ID " + attachmentId);
        }

        HttpHeaders headers = getHttpHeaders(attachment);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @Override
    public List<Attachment> findAllById(List<Long> attachmentIds) {
        return attachmentRepository.findAllById(attachmentIds);
    }

    @Override
    public String getAttachmentUrlById(long attachmentId) {
        return LOAD_IMG_URL + attachmentId;
    }

    @Override
    public void deleteById(Long attachmentId) {
        // ToDo => delete from storage if needed
    }

    @Override
    @SneakyThrows(
            IOException.class
    )
    public ResponseEntity<ResourceRegion> streamVideo(Long attachmentId, HttpHeaders headers) {
        VideoAttachment videoAttachment = (VideoAttachment) getById(attachmentId);

        File videoFile = new File(videoAttachment.getFileUrl());
        Resource videoResource = new FileSystemResource(videoFile);
        long contentLength = videoResource.contentLength();

        ResourceRegion region = getResourceRegion(headers, contentLength, videoResource);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(videoResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
    }

    private static ResourceRegion getResourceRegion(HttpHeaders headers, long contentLength, Resource videoResource) {
        final long CHUNK_SIZE = 1024 * 1024;

        List<HttpRange> ranges = headers.getRange();
        ResourceRegion region;

        if (!ranges.isEmpty()) {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);

            region = new ResourceRegion(videoResource, start, rangeLength);
        } else {
            region = new ResourceRegion(videoResource, 0, Math.min(CHUNK_SIZE, contentLength));
        }
        return region;
    }

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

    private void uploadToStorage(MultipartFile file, Attachment attachment) throws IOException {
        Path path = Paths.get(attachment.getFileUrl());
        Files.write(path, file.getBytes());
    }

    private Attachment uploadAttachmentToDb(MultipartFile file) {
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        file.getContentType();
        if (file.getContentType().startsWith("video/")) {
            VideoAttachment videoAttachment = new VideoAttachment();
            videoAttachment.setFileName(file.getOriginalFilename());
            videoAttachment.setExtension(extension);
            videoAttachment.setFileType(file.getContentType());
            videoAttachment.setAttachmentType(AttachmentType.VIDEO);
            videoAttachment.setDuration(getVideoDuration(file));
            videoAttachment.setResolution(getVideoResolution(file));
            attachmentRepository.save(videoAttachment);
            String fileUrl = generateFileName(videoAttachment);
            videoAttachment.setFileUrl(fileUrl);
            return attachmentRepository.saveAndFlush(videoAttachment);
        } else if (file.getContentType().startsWith("image/")) {
            ImageAttachment imageAttachment = new ImageAttachment();
            imageAttachment.setFileName(file.getOriginalFilename());
            imageAttachment.setExtension(extension);
            imageAttachment.setFileType(file.getContentType());
            imageAttachment.setAttachmentType(AttachmentType.IMAGE);
            attachmentRepository.save(imageAttachment);
            String fileUrl = generateFileName(imageAttachment);
            imageAttachment.setFileUrl(fileUrl);
            return attachmentRepository.saveAndFlush(imageAttachment);
        } else {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setExtension(extension);
            attachment.setFileType(file.getContentType());
            attachment.setAttachmentType(AttachmentType.OTHER);
            attachmentRepository.save(attachment);
            String fileUrl = generateFileName(attachment);
            attachment.setFileUrl(fileUrl);
            return attachmentRepository.saveAndFlush(attachment);
        }
    }

    private Long getVideoDuration(MultipartFile file) {
        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();
        ContentHandler handler = new BodyContentHandler();

        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, handler, metadata, context);
            String durationStr = metadata.get("xmpDM:duration");
            if (durationStr != null) {
                double durationMillis = Double.parseDouble(durationStr);
                return (long) durationMillis;
            }
        } catch (Exception e) {
            throw new CustomConflictException("Failed to get video duration");
        }
        return 0L;
    }

    private String getVideoResolution(MultipartFile file) {
        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();
        ContentHandler handler = new BodyContentHandler();

        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, handler, metadata, context);
            String width = metadata.get("tiff:ImageWidth");
            String height = metadata.get("tiff:ImageLength");

            if (width == null) {
                width = metadata.get("width");
            }
            if (height == null) {
                height = metadata.get("height");
            }

            if (width != null && height != null) {
                return width + "x" + height;
            }
        } catch (Exception e) {
            throw new CustomConflictException("Failed to get video resolution");
        }
        return "Unknown";
    }

    private String generateFileName(Attachment attachment) {
        return URL_TO_SAVE_FILE + "/" + UUID.randomUUID() + "-" + System.currentTimeMillis()
                + "." + attachment.getExtension();
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index > 0)
            return fileName.substring(index + 1);
        return null;
    }
}