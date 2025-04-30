package uz.academy.exam.Exam.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
@PropertySource("classpath:jwt.properties")
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
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            String username,
            String password,
            HttpServletResponse response
    ) {
        // 1) Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2) Load full user details
        User user = userService.findUserByUsername(username);

        // 3) Generate tokens
        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(user));
        String refreshToken = jwtService.generateRefreshToken(new CustomUserDetails(user));

        // 4) Set refresh token in HttpOnly cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);                // only over HTTPS
        cookie.setPath("/api/v1/auth");           // only send for these endpoints
        cookie.setMaxAge((int) (jwtService.refreshTokenExpirationMs / 1000));
        response.addCookie(cookie);

        // 5) Build response body with access token
        JwtResponse body = JwtResponse.builder()
                .token(accessToken)
                .type("Bearer")
                .user(UserJwtPreview.builder()
                        .id(user.getId())
                        .username(user.getUserName())
                        .firstname(user.getFirstName())
                        .image(imageAttachmentMapper.toImageAttachmentResponse((ImageAttachment) user.getProfileImage()))
                        .build())
                .build();

        return ResponseEntity.ok(
                ApiResponse.<JwtResponse>builder()
                        .message("Logged in")
                        .success(true)
                        .data(body)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity
                    .status(401)
                    .body(ApiResponse.<JwtResponse>builder()
                            .message("No refresh token")
                            .success(false)
                            .build());
        }

        String refreshToken = null;
        for (Cookie c : cookies) {
            if ("refreshToken".equals(c.getName())) {
                refreshToken = c.getValue();
            }
        }

        if (refreshToken == null || !jwtService.validateToken(refreshToken))
            return ResponseEntity
                    .status(401)
                    .body(ApiResponse.<JwtResponse>builder()
                            .message("Invalid refresh token")
                            .success(false)
                            .build());

        String username = jwtService.extractUsername(refreshToken);
        User user = userService.findUserByUsername(username);
        String newAccess = jwtService.generateAccessToken(new CustomUserDetails(user));
        String newRefresh = jwtService.generateRefreshToken(new CustomUserDetails(user));

        Cookie cookie = new Cookie("refreshToken", newRefresh);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge((int) (jwtService.refreshTokenExpirationMs / 1000));
        response.addCookie(cookie);

        JwtResponse body = JwtResponse.builder()
                .token(newAccess)
                .type("Bearer")
                .build();

        return ResponseEntity.ok(
                ApiResponse.<JwtResponse>builder()
                        .message("Token refreshed")
                        .success(true)
                        .data(body)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Logged out")
                        .success(true)
                        .build()
        );
    }
}