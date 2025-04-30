package uz.academy.exam.Exam.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String email;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String oldPassword;
}