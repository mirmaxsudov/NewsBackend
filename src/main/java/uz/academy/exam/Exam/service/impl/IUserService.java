package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.model.entity.User;
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
}