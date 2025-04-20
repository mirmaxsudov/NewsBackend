package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.model.response.post.SendPostPageResponseOwn;
import uz.academy.exam.Exam.model.response.post.SendPostResponse;
import uz.academy.exam.Exam.security.service.CustomUserDetails;
import uz.academy.exam.Exam.service.base.post.SendPostService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/send-post")
public class SendPostController {
    private final SendPostService sendPostService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> createSendPost(
            @RequestBody SendPostRequest request,
            @AuthenticationPrincipal CustomUserDetails details
    ) {
        return sendPostService.createSendPost(request, details);
    }

    @PatchMapping("/viewed/{id}")
    public ResponseEntity<ApiResponse<String>> viewed(
            @PathVariable("id") long id
    ) {
        return sendPostService.viewed(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<SendPostPageResponseOwn>> getSendPostsWithPage(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "12", required = false) int size,
            @AuthenticationPrincipal CustomUserDetails details
    ) {
        return sendPostService.getSendPostsWithPage(page, size, details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SendPostResponse>> getById(
            @PathVariable("id") Long id
    ) {
        return sendPostService.getById(id);
    }
}