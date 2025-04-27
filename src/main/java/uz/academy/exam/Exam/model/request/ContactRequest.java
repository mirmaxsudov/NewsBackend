package uz.academy.exam.Exam.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {
    private String subject;
    private String name;
    private String email;
    private String explanation;
    private Long imageId;
}