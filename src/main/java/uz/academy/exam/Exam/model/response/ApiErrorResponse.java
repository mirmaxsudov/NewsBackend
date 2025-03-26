package uz.academy.exam.Exam.model.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;
    private int code;
}