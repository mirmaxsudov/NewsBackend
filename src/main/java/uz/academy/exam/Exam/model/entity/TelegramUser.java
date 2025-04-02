package uz.academy.exam.Exam.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

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
}