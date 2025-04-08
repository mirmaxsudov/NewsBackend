package uz.academy.exam.Exam.mapper.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.academy.exam.Exam.mapper.attachment.ImageAttachmentMapper;
import uz.academy.exam.Exam.model.entity.post.SendPost;
import uz.academy.exam.Exam.model.preview.SendPostPreview;
import uz.academy.exam.Exam.model.response.SendPostPageResponseOwn;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SendPostMapper {
    private final ImageAttachmentMapper imageAttachmentMapper;

    public SendPostPageResponseOwn toSendPostPageResponse(List<SendPost> content, long totalElements) {
        return SendPostPageResponseOwn.builder()
                .totalElements(totalElements)
                .content(toSendPostPewviewList(content))
                .last(false)
                .build();
    }

    public List<SendPostPreview> toSendPostPewviewList(List<SendPost> content) {
        return content.stream().map(this::toSendPostPreview).toList();
    }

    private SendPostPreview toSendPostPreview(SendPost sendPost) {
        return SendPostPreview.builder()
                .id(sendPost.getId())
                .title(sendPost.getTitle())
                .body(sendPost.getPostBody())
                .image(imageAttachmentMapper.toImageAttachmentResponse(sendPost.getImage()))
                .build();
    }
}