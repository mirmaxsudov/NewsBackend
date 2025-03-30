package uz.academy.exam.Exam.service.base;

public interface AuthService {
    String authenticate(String code);

    String login(String phoneNumber, String password);
}