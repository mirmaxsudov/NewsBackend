package uz.academy.exam.Exam.model.preview;

import lombok.*;
import uz.academy.exam.Exam.model.response.ImageAttachmentResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostPreview {
    private Long id;
    private String title;
    private String body;
    private ImageAttachmentResponse image;
}