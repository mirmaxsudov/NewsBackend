package uz.academy.exam.Exam.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostRequest {
    private String title;
    private String postBody;
    private String tags;
    private Long imageId;
}