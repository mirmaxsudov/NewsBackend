package uz.academy.exam.Exam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.academy.exam.Exam.model.entity.attachment.Attachment;
import uz.academy.exam.Exam.model.entity.base.BaseUser;
import uz.academy.exam.Exam.model.enums.UserRole;

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
    @Lob
    @Column(columnDefinition = "TEXT")
    private String explanation;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToOne
    private Attachment profileImage;
    @ManyToOne
    private Attachment bannerImage;
}