package uz.academy.exam.Exam.mapper.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.response.ImageAttachmentResponse;

@Component
@RequiredArgsConstructor
public class ImageAttachmentMapper {
    public ImageAttachmentResponse toImageAttachmentResponse(ImageAttachment image) {
        if (image == null)
            return null;

        return ImageAttachmentResponse.builder()
                .width(image.getWidth() == null ? 0 : image.getWidth())
                .height(image.getHeight() == null ? 0 : image.getHeight())
                .url("http://localhost:8081/api/v1/attachment/" + image.getId())
                .downloadUrl(image.getFileUrl())
                .extension(image.getExtension())
                .id(image.getId())
                .build();
    }
}