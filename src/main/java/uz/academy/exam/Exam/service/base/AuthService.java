package uz.academy.exam.Exam.service.base;

import org.springframework.http.ResponseEntity;
import uz.academy.exam.Exam.model.response.jwt.JwtResponse;
import uz.academy.exam.Exam.model.response.response.ApiResponse;

public interface AuthService {
    ResponseEntity<ApiResponse<Void>> authenticate(String code, String password);

    ResponseEntity<ApiResponse<JwtResponse>> login(String username, String password);
}