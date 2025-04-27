package uz.academy.exam.Exam.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;
import uz.academy.exam.Exam.model.entity.base.Base;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends Base {
    private String subject;
    private String name;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String explanation;
    @ManyToOne
    private ImageAttachment image;
}