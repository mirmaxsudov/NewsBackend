package uz.academy.exam.Exam.service.base;

import uz.academy.exam.Exam.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByLogin(String email);
}
