package uz.academy.exam.Exam.service.base.post;

import org.springframework.http.ResponseEntity;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.model.response.post.SendPostPageResponseOwn;
import uz.academy.exam.Exam.model.response.post.SendPostResponse;
import uz.academy.exam.Exam.security.service.CustomUserDetails;

public interface SendPostService {
    ResponseEntity<ApiResponse<Void>> createSendPost(SendPostRequest request, CustomUserDetails details);

    ResponseEntity<ApiResponse<SendPostPageResponseOwn>> getSendPostsWithPage(int page, int size, CustomUserDetails details);

    ResponseEntity<ApiResponse<SendPostResponse>> getById(Long id);

    ResponseEntity<ApiResponse<String>> viewed(long id);
}