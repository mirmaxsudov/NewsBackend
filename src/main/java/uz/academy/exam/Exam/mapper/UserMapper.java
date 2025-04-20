package uz.academy.exam.Exam.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.academy.exam.Exam.mapper.attachment.ImageAttachmentMapper;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.preview.UserPreview;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ImageAttachmentMapper imageAttachmentMapper;

    public UserPreview toUserPreview(User user) {
        return UserPreview.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .joinedAt(LocalDateTime.now())
                .profileImage(
                        imageAttachmentMapper.toImageAttachmentResponse((ImageAttachment) user.getProfileImage())
                )
                .build();
    }
}