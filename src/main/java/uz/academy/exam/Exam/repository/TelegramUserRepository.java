package uz.academy.exam.Exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.TelegramUser;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    TelegramUser findByChatId(Long chatId);
}