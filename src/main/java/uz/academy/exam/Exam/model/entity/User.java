package uz.academy.exam.Exam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.academy.exam.Exam.model.enums.UserRole;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Attachment attachment;
}