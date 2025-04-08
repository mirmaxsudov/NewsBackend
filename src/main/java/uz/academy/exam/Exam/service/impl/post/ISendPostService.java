package uz.academy.exam.Exam.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.mapper.UserMapper;
import uz.academy.exam.Exam.mapper.post.SendPostMapper;
import uz.academy.exam.Exam.model.entity.User;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.post.SendPost;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.ApiResponse;
import uz.academy.exam.Exam.model.response.SendPostPageResponseOwn;
import uz.academy.exam.Exam.repository.post.SendPostRepository;
import uz.academy.exam.Exam.service.base.AttachmentService;
import uz.academy.exam.Exam.service.base.UserService;
import uz.academy.exam.Exam.service.base.post.SendPostService;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ISendPostService implements SendPostService {
    private final SendPostRepository sendPostRepository;
    private final AttachmentService attachmentService;
    private final SendPostMapper sendPostMapper;
    private final UserService userService;
    private final UserMapper userMapper;

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
        sendPost.setDeletedAt(null);

        sendPostRepository.save(sendPost);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Send post created successfully")
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<SendPostPageResponseOwn>> getSendPostsWithPage(int page, int size) {
        final List<SendPost> content = sendPostRepository.findAll(getPageable(page, size)).getContent();
        final long totalElements = sendPostRepository.count();

        ApiResponse<SendPostPageResponseOwn> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Send posts fetched successfully");

        SendPostPageResponseOwn sendPostPageResponseOwn = sendPostMapper.toSendPostPageResponse(content, totalElements);
        sendPostPageResponseOwn.setPage(page);
        sendPostPageResponseOwn.setSize(size);

        User owner = userService.getById(2L);

        sendPostPageResponseOwn.setOwner(userMapper.toUserPreview(owner));
        sendPostPageResponseOwn.setTotalPages(
                (int) Math.ceil((double) totalElements / size)
        );

        apiResponse.setData(sendPostPageResponseOwn);

        return ResponseEntity.ok(apiResponse);
    }

    private Pageable getPageable(int page, int size) {
        return Pageable.ofSize(size).withPage(page);
    }
}