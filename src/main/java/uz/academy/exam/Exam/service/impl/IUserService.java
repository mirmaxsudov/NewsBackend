package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.academy.exam.Exam.exceptions.CustomNotFoundException;
import uz.academy.exam.Exam.model.entity.attachment.Attachment;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.request.UserUpdateRequest;
import uz.academy.exam.Exam.model.response.ImageAttachmentResponse;
import uz.academy.exam.Exam.model.response.UserResponse;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.repository.user.UserRepository;
import uz.academy.exam.Exam.security.service.CustomUserDetails;
import uz.academy.exam.Exam.service.base.AttachmentService;
import uz.academy.exam.Exam.service.base.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserService implements UserService {
    private final UserRepository userRepository;
    //    private final PasswordEncoder passwordEncoder;
    private final AttachmentService attachmentService;

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

    @Override
    public String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 9000000000L) + 1000000000L);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(
                        () -> new CustomNotFoundException("User not found with username: " + username)
                );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateBanner(Long imageId, CustomUserDetails details) {
        Attachment banner = attachmentService.getById(imageId);

        User user = details.user();

        if (user.getBannerImage() != null)
            attachmentService.deleteById(user.getBannerImage().getId());

        user.setBannerImage(banner);
        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Banner updated successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateProfileImage(Long imageId, CustomUserDetails details) {
        Attachment profileImage = attachmentService.getById(imageId);

        User user = details.user();

        if (user.getProfileImage() != null)
            attachmentService.deleteById(user.getProfileImage().getId());

        user.setProfileImage(profileImage);
        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Profile image updated successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateUser(UserUpdateRequest request, CustomUserDetails details) {
        User user = details.user();

//        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
//            user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setFirstName(request.getFirstname() == null ? user.getFirstName() : request.getFirstname());
        user.setLastName(request.getLastname() == null ? user.getLastName() : request.getLastname());
        user.setEmail(request.getEmail() == null ? user.getEmail() : request.getEmail());
//        user.setUserName(request.getUsername());

        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("User updated successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> updateExplanation(String explanation, CustomUserDetails details) {
        User user = details.user();

        user.setExplanation(explanation);
        save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Explanation updated successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteBanner(CustomUserDetails details) {
        User user = details.user();
        attachmentService.deleteById(user.getBannerImage().getId());
        user.setBannerImage(null);
        save(user);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Banner deleted successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteProfileImage(CustomUserDetails details) {
        User user = details.user();
        attachmentService.deleteById(user.getProfileImage().getId());
        user.setProfileImage(null);
        save(user);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Profile image deleted successfully")
                        .success(true)
                        .data(null)
                        .build()
        );
    }

    private User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException("User not found with id: " + userId)
                );
    }
}