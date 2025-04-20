package uz.academy.exam.Exam.model.entity.post;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.academy.exam.Exam.model.entity.user.User;
import uz.academy.exam.Exam.model.entity.base.Base;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowUser extends Base {
    @ManyToOne
    private User follower;
    @ManyToOne
    private User following;
    private LocalDateTime followedAt;
}