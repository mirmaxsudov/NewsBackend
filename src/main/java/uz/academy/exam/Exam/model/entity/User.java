package uz.academy.exam.Exam.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
    private String explanation;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToOne
    private Attachment attachment;
}