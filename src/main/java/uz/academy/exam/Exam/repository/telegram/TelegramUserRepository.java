package uz.academy.exam.Exam.repository.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramUser;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    TelegramUser findByChatId(Long chatId);
}