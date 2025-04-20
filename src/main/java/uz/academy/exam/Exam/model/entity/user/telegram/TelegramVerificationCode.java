package uz.academy.exam.Exam.model.entity.user.telegram;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.academy.exam.Exam.model.entity.base.Base;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramVerificationCode extends Base {
    private String code;
    private LocalDateTime sentAt = LocalDateTime.now();
    @ManyToOne
    private TelegramUser telegramUser;
}