package uz.academy.exam.Exam.model.response;

import lombok.*;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}