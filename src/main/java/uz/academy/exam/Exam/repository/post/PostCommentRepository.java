package uz.academy.exam.Exam.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.post.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}