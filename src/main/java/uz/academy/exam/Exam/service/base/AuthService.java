package uz.academy.exam.Exam.service.base;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import uz.academy.exam.Exam.model.response.jwt.JwtResponse;
import uz.academy.exam.Exam.model.response.response.ApiResponse;

public interface AuthService {
    ResponseEntity<ApiResponse<Void>> authenticate(String code, String password);

    ResponseEntity<ApiResponse<JwtResponse>> login(String username, String password, HttpServletResponse response);

    ResponseEntity<ApiResponse<JwtResponse>> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response);
}