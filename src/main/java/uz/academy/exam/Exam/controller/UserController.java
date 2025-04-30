package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.academy.exam.Exam.model.request.UserUpdateRequest;
import uz.academy.exam.Exam.model.response.UserResponse;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.security.service.CustomUserDetails;
import uz.academy.exam.Exam.service.base.UserService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PatchMapping("/update-banner/{imageId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateBanner(
            @PathVariable("imageId") Long imageId,
            @AuthenticationPrincipal CustomUserDetails details
    ) {
        return userService.updateBanner(imageId, details);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PatchMapping("/update-profile-image/{imageId}")
    public ResponseEntity<ApiResponse<Void>> updateProfileImage(
            @PathVariable("imageId") Long imageId,
            @AuthenticationPrincipal CustomUserDetails details
    ) {
        return userService.updateProfileImage(imageId, details);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails details
    ) {
        return userService.updateUser(request, details);
    }

    @PatchMapping("/update-explanation")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updateExplanation(
            @RequestParam("explanation") String explanation,
            @AuthenticationPrincipal CustomUserDetails details
    ) {
//        return userService.updateExplanation(explanation, details);
        return null;
    }
}