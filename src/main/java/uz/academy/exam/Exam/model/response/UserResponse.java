package uz.academy.exam.Exam.model.response;

import lombok.*;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.enums.UserRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String explanation;
    private UserRole role;
    private AttachmentResponse profileImage;
    private AttachmentResponse bannerImage;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .username(user.getUserName())
                .email(user.getEmail())
                .explanation(user.getExplanation())
                .role(user.getRole())
                .profileImage(AttachmentResponse.from(user.getProfileImage()))
                .bannerImage(AttachmentResponse.from(user.getBannerImage()))
                .build();
    }
}