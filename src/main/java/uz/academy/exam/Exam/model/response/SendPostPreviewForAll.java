package uz.academy.exam.Exam.model.response;

import lombok.*;
import uz.academy.exam.Exam.model.preview.UserPreview;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostPreviewForAll {
    private Long id;
    private String title;
    private String body;
    private UserPreview owner;
    private ImageAttachmentResponse image;
}