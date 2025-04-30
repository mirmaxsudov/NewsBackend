package uz.academy.exam.Exam.model.entity.user;

import jakarta.persistence.*;
import lombok.*;
import uz.academy.exam.Exam.model.entity.attachment.Attachment;
import uz.academy.exam.Exam.model.entity.base.BaseUser;
import uz.academy.exam.Exam.model.entity.post.SendPost;
import uz.academy.exam.Exam.model.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseUser {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String explanation;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToOne
    private Attachment profileImage;
    @ManyToOne
    private Attachment bannerImage;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SendPost> sendPosts = new ArrayList<>();
}