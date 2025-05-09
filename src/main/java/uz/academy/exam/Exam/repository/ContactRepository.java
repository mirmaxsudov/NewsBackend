package uz.academy.exam.Exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}