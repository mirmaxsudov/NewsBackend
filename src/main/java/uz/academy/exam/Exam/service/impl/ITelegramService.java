package uz.academy.exam.Exam.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.academy.exam.Exam.exceptions.CustomNotFoundException;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramUser;
import uz.academy.exam.Exam.repository.telegram.TelegramUserRepository;
import uz.academy.exam.Exam.repository.telegram.TelegramVerificationCodeRepository;
import uz.academy.exam.Exam.service.base.TelegramUserService;

@Service
@RequiredArgsConstructor
public class ITelegramService implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramVerificationCodeRepository telegramVerificationCodeRepository;

    @Override
    @Cacheable(value = "telegramUser", key = "#chatId")
    public TelegramUser findByChatId(Long chatId) {
        return telegramUserRepository.findByChatId(chatId);
    }

    @Override
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public TelegramUser findTelegramUserByValidVerificationCode(String code) {
        return telegramVerificationCodeRepository.findTelegramUserByValidVerificationCode(code)
                .orElseThrow(() -> new CustomNotFoundException("Verification code not found"));
    }
}