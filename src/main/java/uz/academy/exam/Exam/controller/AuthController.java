package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.academy.exam.Exam.service.base.AuthService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public String authenticate(
            @RequestParam("code") String code
    ) {
        return authService.authenticate(code);
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password
    ) {
        return authService.login(phoneNumber, password);
    }
}