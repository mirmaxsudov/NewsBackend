package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.academy.exam.Exam.model.entity.User;
import uz.academy.exam.Exam.model.entity.base.Base;
import uz.academy.exam.Exam.model.enums.PostRateEnum;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRate extends Base {
    @Enumerated(EnumType.STRING)
    private PostRateEnum rate;
    @ManyToOne
    private Post post;
    @ManyToOne
    private User user;
}