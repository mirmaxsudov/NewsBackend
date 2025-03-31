package uz.academy.exam.Exam.service.base;

import uz.academy.exam.Exam.model.entity.TelegramUser;

public interface TelegramUserService {
    TelegramUser findByChatId(Long chatId);

    void save(TelegramUser telegramUser);
}