package uz.academy.exam.Exam.service.base;

import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.response.UserResponse;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByLogin(String email);

    void save(User user);

    UserResponse getUser(Long userId);

    User getById(long userId);

    String generateVerificationCode();

    boolean existsByUsername(String username);

    User findUserByUsername(String username);
}
