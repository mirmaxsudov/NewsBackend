package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.academy.exam.Exam.exceptions.CustomNotFoundException;
import uz.academy.exam.Exam.model.entity.User;
import uz.academy.exam.Exam.model.response.UserResponse;
import uz.academy.exam.Exam.repository.UserRepository;
import uz.academy.exam.Exam.service.base.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserByLogin(String email) {
        if (email == null || email.isBlank())
            return Optional.empty();

        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public UserResponse getUser(Long userId) {
        return UserResponse.from(getById(userId));
    }

    @Override
    public User getById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new CustomNotFoundException("User not found with id: " + userId)
                );
    }

    private User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException("User not found with id: " + userId)
                );
    }
}