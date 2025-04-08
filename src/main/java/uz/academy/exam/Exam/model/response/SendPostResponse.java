package uz.academy.exam.Exam.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPostResponse {
    private Long id;
    private String title;
}