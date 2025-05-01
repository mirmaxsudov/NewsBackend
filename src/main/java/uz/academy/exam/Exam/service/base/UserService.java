package uz.academy.exam.Exam.service.base;

import org.springframework.http.ResponseEntity;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.request.UserUpdateRequest;
import uz.academy.exam.Exam.model.response.UserResponse;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.security.service.CustomUserDetails;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByLogin(String email);

    void save(User user);

    UserResponse getUser(Long userId);

    User getById(long userId);

    String generateVerificationCode();

    boolean existsByUsername(String username);

    User findUserByUsername(String username);

    ResponseEntity<ApiResponse<Void>> updateBanner(Long imageId, CustomUserDetails details);

    ResponseEntity<ApiResponse<Void>> updateProfileImage(Long imageId, CustomUserDetails details);

    ResponseEntity<ApiResponse<Void>> updateUser(UserUpdateRequest request, CustomUserDetails details);

    ResponseEntity<ApiResponse<Void>> updateExplanation(String explanation, CustomUserDetails details);

    ResponseEntity<ApiResponse<Void>> deleteBanner(CustomUserDetails details);

    ResponseEntity<ApiResponse<Void>> deleteProfileImage(CustomUserDetails details);
}