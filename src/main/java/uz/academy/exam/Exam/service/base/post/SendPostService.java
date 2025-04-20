package uz.academy.exam.Exam.service.base.post;

import org.springframework.http.ResponseEntity;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.model.response.post.SendPostPageResponseOwn;
import uz.academy.exam.Exam.model.response.post.SendPostResponse;

public interface SendPostService {
    ResponseEntity<ApiResponse<Void>> createSendPost(SendPostRequest request);

    ResponseEntity<ApiResponse<SendPostPageResponseOwn>> getSendPostsWithPage(int page, int size);

    ResponseEntity<ApiResponse<SendPostResponse>> getById(Long id);

    ResponseEntity<ApiResponse<String>> viewed(long id);
}