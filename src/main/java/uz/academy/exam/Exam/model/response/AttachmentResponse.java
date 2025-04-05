package uz.academy.exam.Exam.model.response;

import lombok.*;
import uz.academy.exam.Exam.model.entity.Attachment;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private String url;
    private String downloadUrl;

    public static AttachmentResponse from(Attachment attachment) {
        if (attachment != null) {
            return AttachmentResponse.builder()
                    .id(attachment.getId())
                    .url(attachment.getFileUrl())
                    .downloadUrl(attachment.getFileUrl())
                    .build();
        }

        return null;
    }
}