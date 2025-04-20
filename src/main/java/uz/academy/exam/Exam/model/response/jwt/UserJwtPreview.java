package uz.academy.exam.Exam.model.response.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.academy.exam.Exam.model.response.AttachmentResponse;
import uz.academy.exam.Exam.model.response.ImageAttachmentResponse;

@Getter
@Setter
@Builder
public class UserJwtPreview {
    private Long id;
    private String firstname;
    private String username;
    private ImageAttachmentResponse image;
}