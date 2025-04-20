package uz.academy.exam.Exam.model.response.post;

import lombok.*;
import uz.academy.exam.Exam.model.enums.Category;
import uz.academy.exam.Exam.model.preview.UserPreview;
import uz.academy.exam.Exam.model.response.ImageAttachmentResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String title;
    private long views;
    private long postCounts;
    private String postBody;
    private Category category;
    private long commentCounts;
    private List<String> tags = new ArrayList<>();
    private UserPreview owner;
    private ImageAttachmentResponse image;
}