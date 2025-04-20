package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.model.response.jwt.JwtResponse;
import uz.academy.exam.Exam.service.base.AuthService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<Void>> authenticate(
            @RequestParam("code") String code,
            @RequestParam("password") String password
    ) {
        return authService.authenticate(code, password);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return authService.login(username, password);
    }
}