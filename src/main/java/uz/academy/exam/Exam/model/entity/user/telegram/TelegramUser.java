package uz.academy.exam.Exam.model.entity.user.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import uz.academy.exam.Exam.model.entity.base.BaseUser;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser extends BaseUser {
    @Column(unique = true, nullable = false, name = "chat_id")
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean isRegistered = false;
    @OneToMany(mappedBy = "telegramUser")
    private List<TelegramVerificationCode> verificationCodes;
}