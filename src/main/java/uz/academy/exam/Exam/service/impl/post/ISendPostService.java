package uz.academy.exam.Exam.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.post.SendPost;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.ApiResponse;
import uz.academy.exam.Exam.repository.post.SendPostRepository;
import uz.academy.exam.Exam.service.base.AttachmentService;
import uz.academy.exam.Exam.service.base.post.SendPostService;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ISendPostService implements SendPostService {
    private final SendPostRepository sendPostRepository;
    private final AttachmentService attachmentService;

    @Override
    public ResponseEntity<ApiResponse<Void>> createSendPost(SendPostRequest request) {
        ImageAttachment imageAttachment = (ImageAttachment) attachmentService.getById(request.getImageId());

        SendPost sendPost = new SendPost();
        sendPost.setTitle(request.getTitle());
        sendPost.setPostBody(request.getPostBody());
        sendPost.setTags(Arrays.stream(request.getTags().split(" ")).toList());
        sendPost.setImage(imageAttachment);
        sendPost.setDraft(false);
        sendPost.setDeleted(false);
        sendPost.setViews(0L);
        sendPost.setDeleted(false);
        sendPost.setDeletedAt(null); // or simply do not set deletedAt for new posts

        sendPostRepository.save(sendPost);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Send post created successfully")
                        .build()
        );
    }
}