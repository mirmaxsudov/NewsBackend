package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.service.base.AuthService;
import uz.academy.exam.Exam.service.base.UserService;

@Service
@RequiredArgsConstructor
public class IAuthService implements AuthService {
    private final UserService userService;

    @Override
    public String authenticate(String code) {
        return "";
    }

    @Override
    public String login(String phoneNumber, String password) {
        return "";
    }
}