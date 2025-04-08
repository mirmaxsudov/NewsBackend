package uz.academy.exam.Exam.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.ApiResponse;
import uz.academy.exam.Exam.model.response.SendPostPageResponseOwn;
import uz.academy.exam.Exam.service.base.post.SendPostService;
import uz.academy.exam.Exam.util.ApiUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUtil.BASE_API_VERSION + "/send-post")
public class SendPostController {
    private final SendPostService sendPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createSendPost(
            @RequestBody SendPostRequest request
    ) {
        return sendPostService.createSendPost(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SendPostPageResponseOwn>> getSendPostsWithPage(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "12", required = false) int size
    ) {
        return sendPostService.getSendPostsWithPage(page, size);
    }
}