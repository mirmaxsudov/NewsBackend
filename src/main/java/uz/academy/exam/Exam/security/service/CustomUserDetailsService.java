package uz.academy.exam.Exam.security.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.service.base.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findUserByLogin(email);

        if (userOptional.isEmpty())
            throw new UsernameNotFoundException("User not found");

        User user = userOptional.get();

        return new CustomUserDetails(user);
    }
}
