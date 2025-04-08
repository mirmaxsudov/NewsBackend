package uz.academy.exam.Exam.model.preview;

import lombok.*;
import uz.academy.exam.Exam.model.response.ImageAttachmentResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreview {
    private Long id;
    private String firstname;
    private LocalDateTime joinedAt;
    private ImageAttachmentResponse profileImage;
}