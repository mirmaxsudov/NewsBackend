package uz.academy.exam.Exam.service.impl.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.academy.exam.Exam.exceptions.CustomBadRequestException;
import uz.academy.exam.Exam.exceptions.CustomNotFoundException;
import uz.academy.exam.Exam.mapper.UserMapper;
import uz.academy.exam.Exam.mapper.post.SendPostMapper;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.post.PostRate;
import uz.academy.exam.Exam.model.entity.post.SendPost;
import uz.academy.exam.Exam.model.enums.Category;
import uz.academy.exam.Exam.model.enums.PostRateEnum;
import uz.academy.exam.Exam.model.request.SendPostRequest;
import uz.academy.exam.Exam.model.response.SendPostPageResponseAll;
import uz.academy.exam.Exam.model.response.response.ApiResponse;
import uz.academy.exam.Exam.model.response.post.SendPostPageResponseOwn;
import uz.academy.exam.Exam.model.response.post.SendPostResponse;
import uz.academy.exam.Exam.repository.post.SendPostRepository;
import uz.academy.exam.Exam.security.service.CustomUserDetails;
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

    @PersistenceContext
    private final EntityManager em;

    @Override
    public ResponseEntity<ApiResponse<Void>> createSendPost(SendPostRequest request, CustomUserDetails details) {
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
        sendPost.setOwner(details.user());

        sendPostRepository.save(sendPost);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Send post created successfully")
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<SendPostPageResponseOwn>> getSendPostsWithPage(
            int page,
            int size,
            CustomUserDetails details
    ) {
        long userId = details.user().getId();
        Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());

        Page<SendPost> pageResult = sendPostRepository.findByOwnerId(userId, paging);

        List<SendPost> content = pageResult.getContent();
        int totalPages = pageResult.getTotalPages();
        long totalItems = pageResult.getTotalElements();

        SendPostPageResponseOwn dto = sendPostMapper.toSendPostPageResponse(
                content,
                totalItems
        );
        dto.setPage(page);
        dto.setSize(size);
        dto.setTotalPages(totalPages);
        dto.setOwner(userMapper.toUserPreview(details.user()));

        ApiResponse<SendPostPageResponseOwn> resp = ApiResponse.<SendPostPageResponseOwn>builder()
                .success(true)
                .message("Send posts fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<ApiResponse<SendPostResponse>> getById(Long id) {
        SendPost sendPost = getByIdForBackend(id);
        SendPostResponse response = sendPostMapper.toSendPostResponse(sendPost);

        return ResponseEntity.ok(
                ApiResponse.<SendPostResponse>builder()
                        .message("Send post fetched successfully")
                        .data(response)
                        .success(true)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<String>> viewed(long id) {
        SendPost sendPost = getByIdForBackend(id);
        sendPost.setViews(sendPost.getViews() + 1);
        sendPostRepository.save(sendPost);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Send post viewed successfully")
                        .success(true)
                        .data("Send post viewed successfully")
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<SendPostPageResponseAll>> getRelatedSendPosts(Long postId, int page, int size) {
        SendPost post = getByIdForBackend(postId);
        Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());
        Page<SendPost> pageResult = sendPostRepository.findByTags(post.getTags(), post.getId(), paging);

        List<SendPost> content = pageResult.getContent();
        int totalPages = pageResult.getTotalPages();
        long totalItems = pageResult.getTotalElements();

        SendPostPageResponseAll dto = sendPostMapper.toSendPostPageResponseForAll(
                content,
                totalItems
        );

        dto.setPage(page);
        dto.setSize(size);
        dto.setTotalPages(totalPages);

        ApiResponse<SendPostPageResponseAll> resp = ApiResponse.<SendPostPageResponseAll>builder()
                .success(true)
                .message("Send posts fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<ApiResponse<SendPostPageResponseAll>> getTopSendPosts(int page, int size) {
        Pageable paging = getPageable(page, size);
        Page<SendPost> pageResult = sendPostRepository.findByIsDraftFalseOrderByViewsDesc(paging);

        List<SendPost> content = pageResult.getContent();
        int totalPages = pageResult.getTotalPages();
        long totalItems = pageResult.getTotalElements();

        SendPostPageResponseAll dto = sendPostMapper.toSendPostPageResponseForAll(
                content,
                totalItems
        );

        dto.setPage(page);
        dto.setSize(size);
        dto.setTotalPages(totalPages);

        ApiResponse<SendPostPageResponseAll> resp = ApiResponse.<SendPostPageResponseAll>builder()
                .success(true)
                .message("Send posts fetched successfully")
                .data(dto)
                .build();

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<ApiResponse<SendPostPageResponseAll>> getByCategory(
            Category category,
            int page,
            int size,
            boolean isNew,
            boolean isTrendy,
            boolean isPopular,
            boolean isTop
    ) {
        int flags = (isNew ? 1 : 0) + (isTrendy ? 1 : 0) + (isPopular ? 1 : 0) + (isTop ? 1 : 0);

        if (flags != 1)
            throw new CustomBadRequestException("Exactly one of isNew, isTrendy, isPopular or isTop must be true");

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<SendPost> cq = cb.createQuery(SendPost.class);
        Root<SendPost> root = cq.from(SendPost.class);
        cq.where(cb.equal(root.get("category"), category));

        if (isNew) {
            cq.orderBy(cb.desc(root.get("createdAt")));
        } else if (isPopular) {
            cq.orderBy(cb.desc(root.get("views")));
        } else if (isTrendy) {
            Subquery<Long> countSub = cq.subquery(Long.class);
            Root<PostRate> rateR = countSub.from(PostRate.class);
            countSub.select(cb.count(rateR))
                    .where(cb.equal(rateR.get("post"), root));
            cq.orderBy(cb.desc(countSub));
        } else {
            Page<SendPost> pq = sendPostRepository.findTopRatedByCategory(
                    category,
                    PageRequest.of(page, size)
            );

            SendPostPageResponseAll dto = sendPostMapper.toSendPostPageResponseForAll(pq.getContent(), pq.getTotalElements());

            return ResponseEntity.ok(ApiResponse.<SendPostPageResponseAll>builder()
                    .success(true)
                    .message("Send posts fetched successfully")
                    .data(dto)
                    .build());
        }

        TypedQuery<SendPost> query = em.createQuery(cq)
                .setFirstResult(page * size)
                .setMaxResults(size);

        List<SendPost> posts = query.getResultList();

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<SendPost> countRoot = countCq.from(SendPost.class);
        countCq.select(cb.count(countRoot))
                .where(cb.equal(countRoot.get("category"), category));
        long totalElements = em.createQuery(countCq).getSingleResult();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        SendPostPageResponseAll dto = sendPostMapper.toSendPostPageResponseForAll(posts, totalElements);
        dto.setTotalPages(totalPages);

        return ResponseEntity.ok(ApiResponse.<SendPostPageResponseAll>builder()
                .success(true)
                .message("Send posts fetched successfully")
                .data(dto)
                .build());
    }

    private Pageable getPageable(int page, int size) {
        return Pageable.ofSize(size).withPage(page);
    }

    public SendPost getByIdForBackend(Long id) {
        return sendPostRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("There is no send post with this id " + id));
    }
}