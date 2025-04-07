package uz.academy.exam.Exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.attachment.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}