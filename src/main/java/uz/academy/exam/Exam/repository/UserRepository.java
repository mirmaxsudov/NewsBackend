package uz.academy.exam.Exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}