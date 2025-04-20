package uz.academy.exam.Exam.repository.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramUser;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramVerificationCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramVerificationCodeRepository extends JpaRepository<TelegramVerificationCode, Long> {
    List<TelegramVerificationCode> findAllByTelegramUser(TelegramUser telegramUser);

    @Modifying
    @Transactional
    @Query("delete from TelegramVerificationCode t where t.sentAt < :time")
    void deleteAllBySentAtBefore(@Param("time") LocalDateTime time);

    @Query("select t from TelegramUser as t join TelegramVerificationCode as v on t.id = v.telegramUser.id where v.code = :code")
    Optional<TelegramUser> findTelegramUserByValidVerificationCode(String code);
}