package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.enums.Category;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.SendPostPageResponseAll;
import uz.academy.exam.Exam.model.response.SendPostPreviewForAll;
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

    @GetMapping("/related/{id}")
    public ResponseEntity<ApiResponse<SendPostPageResponseAll>> getRelatedSendPosts(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "12", required = false) int size
    ) {
        return sendPostService.getRelatedSendPosts(id, page, size);
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<SendPostPageResponseAll>> getTopSendPosts(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "12", required = false) int size
    ) {
        return sendPostService.getTopSendPosts(page, size);
    }

    @GetMapping("/{category}/category")
    public ResponseEntity<ApiResponse<SendPostPageResponseAll>> getByCategory(
            @PathVariable("category") Category category,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "12", required = false) int size,
            @RequestParam(value = "isNew", defaultValue = "true", required = false) Boolean isNew,
            @RequestParam(value = "isTrendy", defaultValue = "false", required = false) Boolean isTrendy,
            @RequestParam(value = "isPopular", defaultValue = "false", required = false) Boolean isPopular,
            @RequestParam(value = "isTop", defaultValue = "false", required = false) Boolean isTop
    ) {
        return sendPostService.getByCategory(category, page, size, isNew, isTrendy, isPopular, isTop);
    }
}