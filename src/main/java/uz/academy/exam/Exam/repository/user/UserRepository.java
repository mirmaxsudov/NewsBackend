package uz.academy.exam.Exam.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("select count(1) > 0 from User u where u.userName = :userName")
    boolean existsByUserName(@Param("userName") String username);

    @Query("select u from User u where u.userName = :userName")
    Optional<User> findByUserName(@Param("userName") String username);
}