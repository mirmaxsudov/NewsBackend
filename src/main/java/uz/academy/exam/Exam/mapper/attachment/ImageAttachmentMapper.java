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
                .url(image.getFileUrl())
                .downloadUrl(image.getFileUrl())
                .extension(image.getExtension())
                .id(image.getId())
                .build();
    }
}