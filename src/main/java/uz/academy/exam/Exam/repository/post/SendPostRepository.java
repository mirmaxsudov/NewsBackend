package uz.academy.exam.Exam.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.post.SendPost;
import uz.academy.exam.Exam.model.enums.Category;

import java.util.List;

@Repository
public interface SendPostRepository extends JpaRepository<SendPost, Long> {
    Page<SendPost> findByOwnerId(long userId, Pageable paging);

    @Query("SELECT p FROM SendPost p JOIN p.tags t WHERE t IN :tags AND p.id != :postId")
    Page<SendPost> findByTags(@Param("tags") List<String> tags, @Param("postId") Long postId, Pageable pageable);

    Page<SendPost> findByIsDraftFalseOrderByViewsDesc(Pageable pageable);

    @Query(
            value = "SELECT p " +
                    "FROM SendPost p LEFT JOIN p.rates r " +
                    "WHERE p.category = :cat " +
                    "GROUP BY p " +
                    "ORDER BY AVG( " +
                    "  CASE r.rate " +
                    "    WHEN uz.academy.exam.Exam.model.enums.PostRateEnum.FIVE_STAR  THEN 5 " +
                    "    WHEN uz.academy.exam.Exam.model.enums.PostRateEnum.FOUR_STAR  THEN 4 " +
                    "    WHEN uz.academy.exam.Exam.model.enums.PostRateEnum.THREE_STAR THEN 3 " +
                    "    WHEN uz.academy.exam.Exam.model.enums.PostRateEnum.TWO_STAR   THEN 2 " +
                    "    WHEN uz.academy.exam.Exam.model.enums.PostRateEnum.ONE_STAR   THEN 1 " +
                    "    ELSE 0 " +
                    "  END" +
                    ") DESC"
    )
    Page<SendPost> findTopRatedByCategory(
            @Param("cat") Category category,
            Pageable pageable
    );
}