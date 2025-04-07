package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.academy.exam.Exam.model.entity.User;
import uz.academy.exam.Exam.model.entity.base.Base;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostComment extends Base {
    private String body;
    private boolean isReplyComment;

    @ManyToOne
    private PostComment parentComment;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Post post;  // Added association to Post
}
