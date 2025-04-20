package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.exceptions.CustomBadRequestException;
import uz.academy.exam.Exam.exceptions.CustomConflictException;
import uz.academy.exam.Exam.mapper.attachment.ImageAttachmentMapper;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramUser;
import uz.academy.exam.Exam.model.enums.UserRole;
import uz.academy.exam.Exam.model.response.jwt.JwtResponse;
import uz.academy.exam.Exam.model.response.jwt.UserJwtPreview;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.security.service.CustomUserDetails;
import uz.academy.exam.Exam.security.service.JwtService;
import uz.academy.exam.Exam.service.base.AuthService;
import uz.academy.exam.Exam.service.base.TelegramUserService;
import uz.academy.exam.Exam.service.base.UserService;

@Service
@RequiredArgsConstructor
public class IAuthService implements AuthService {
    private final TelegramUserService telegramUserService;
    private final AuthenticationManager authenticationManager;
    private final ImageAttachmentMapper imageAttachmentMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<ApiResponse<Void>> authenticate(String code, String password) {
        TelegramUser telegramUser = telegramUserService.findTelegramUserByValidVerificationCode(code);

        if (userService.existsByUsername(telegramUser.getUsername()))
            throw new CustomConflictException("User with username " + telegramUser.getUsername() + " already exists");

        // Changed telegram user to registered
        telegramUser.setRegistered(true);
        telegramUserService.save(telegramUser);

        // Create new user
        User user = User.builder()
                .role(UserRole.USER)
                .userName(telegramUser.getUsername())
                .firstName(telegramUser.getFirstName())
                .lastName(telegramUser.getLastName())
                .password(passwordEncoder.encode(password))
                .profileImage(null)
                .bannerImage(null)
                .build();

        userService.save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("User registered successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<JwtResponse>> login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password
                )
        );

        User user = userService.findUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CustomBadRequestException("Wrong password");

        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(user));

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(accessToken)
                .type("Bearer")
                .build();

        UserJwtPreview preview = UserJwtPreview.builder()
                .id(user.getId())
                .username(user.getUserName())
                .firstname(user.getFirstName())
                .image(imageAttachmentMapper.toImageAttachmentResponse((ImageAttachment) user.getProfileImage()))
                .build();

        jwtResponse.setUser(preview);

        return ResponseEntity.status(200).body(
                ApiResponse.<JwtResponse>builder()
                        .message("User logged in successfully")
                        .success(true)
                        .data(jwtResponse)
                        .build()
        );
    }
}