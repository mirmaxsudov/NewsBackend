package uz.academy.exam.Exam.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.post.SendPost;

@Repository
public interface SendPostRepository extends JpaRepository<SendPost, Long> {
    Page<SendPost> findByOwnerId(long userId, Pageable paging);
}