package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.academy.exam.Exam.model.entity.attachment.ImageAttachment;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPost extends Post {
    @ManyToOne
    private ImageAttachment image;
}