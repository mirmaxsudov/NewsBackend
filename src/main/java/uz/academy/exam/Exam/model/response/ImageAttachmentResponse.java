package uz.academy.exam.Exam.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAttachmentResponse {
    private int width;
    private int height;
    private String url;
    private String downloadUrl;
    private String extension;
}