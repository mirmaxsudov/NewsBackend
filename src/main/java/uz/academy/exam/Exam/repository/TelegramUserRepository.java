package uz.academy.exam.Exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.academy.exam.Exam.model.entity.TelegramUser;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    @Query(value = "select * from telegram_user where id chat_id = :chatId", nativeQuery = true)
    TelegramUser findByChatId(@Param("chatId") Long chatId);
}