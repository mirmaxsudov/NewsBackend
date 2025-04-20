package uz.academy.exam.Exam.model.response.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {
    private UserJwtPreview user;
    private String token;
    private String type = "Bearer";
}