package uz.academy.exam.Exam.service.bot;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import uz.academy.exam.Exam.config.bot.BotConfiguration;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramUser;
import uz.academy.exam.Exam.model.entity.user.telegram.TelegramVerificationCode;
import uz.academy.exam.Exam.repository.telegram.TelegramVerificationCodeRepository;
import uz.academy.exam.Exam.service.base.TelegramUserService;
import uz.academy.exam.Exam.service.base.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfiguration botConfiguration;
    private final UserService userService;
    private final TelegramVerificationCodeRepository telegramVerificationCodeRepository;
    private final TelegramUserService telegramUserService;
    private final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);
    private final static Short MAX_GET_VERIFICATION_CODE_COUNT = 3;

    @Autowired
    public TelegramBot(BotConfiguration botConfiguration, UserService userService, TelegramVerificationCodeRepository telegramVerificationCodeRepository, TelegramUserService telegramUserService) {
        this.botConfiguration = botConfiguration;
        this.userService = userService;
        this.telegramVerificationCodeRepository = telegramVerificationCodeRepository;
        this.telegramUserService = telegramUserService;

        List<BotCommand> commands = List.of(
                new BotCommand("/start", "Start Bot 🔰"),
                new BotCommand("/help", "Help 🆘"),
                new BotCommand("/code", "Get verification code 🔑"),
                new BotCommand("/delete", "Delete your account 🗑")
        );

        try {
            execute(new SetMyCommands(
                    commands,
                    new BotCommandScopeDefault(),
                    null
            ));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery())
            return;

        processMessage(update.getMessage());
    }

    @SneakyThrows
    private void processMessage(Message message) {
        final String text = message.getText();
        final Long chatId = message.getChatId();

        if (text == null || chatId == null || text.isBlank())
            return;

        TelegramUser telegramUser = telegramUserService.findByChatId(chatId);;

        if (telegramUser == null) {
            registerLogic(message, text, chatId);
            return;
        } else if (text.equals("/code")) {
            getVerificationCode(chatId, telegramUser);
        }
    }

    private void getVerificationCode(Long chatId, TelegramUser telegramUser) {
        if (telegramUser.isRegistered()) {
            sendTextMessage(chatId, "You are already registered", false);
            return;
        }

        List<TelegramVerificationCode> verificationCodes = telegramVerificationCodeRepository.findAllByTelegramUser(telegramUser);

        if (MAX_GET_VERIFICATION_CODE_COUNT - verificationCodes.size() <= 0) {
            sendTextMessage(chatId, "You can get verification code only " + MAX_GET_VERIFICATION_CODE_COUNT + " times.", false);
            return;
        }

        TelegramVerificationCode code = new TelegramVerificationCode();
        code.setCode(userService.generateVerificationCode());
        code.setTelegramUser(telegramUser);
        code.setSentAt(LocalDateTime.now());

        telegramVerificationCodeRepository.save(code);

        sendTextMessage(chatId, "Verification code: <pre>" + code.getCode() + "</pre>", true);
    }

    private void registerLogic(Message message, String text, Long chatId) {
        if (text.equals("/start")) {
            User from = message.getFrom();
            String username = from.getUserName();
            String firstName = from.getFirstName();
            String lastName = from.getLastName();

            TelegramUser telegramUser = TelegramUser.builder()
                    .chatId(chatId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(username)
                    .build();

            telegramUserService.save(telegramUser);
            sendTextMessage(chatId, "You have successfully registered in the bot!.\nUse /help command to get help.", false);

        } else
            sendTextMessage(chatId, "You are not registered in the bot!.\nUse /start command to register.", false);
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }

    @Override
    @SuppressWarnings("all")
    public String getBotToken() {
        return botConfiguration.getBotToken();
    }

    @SneakyThrows
    public void sendTextMessage(Long chatId, String text, boolean isHtml) {
        SendMessage build = SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .build();

        if (isHtml)
            build.setParseMode(ParseMode.HTML);

        execute(build);
    }

    @Scheduled(fixedRate = 60000L) // Each one minute
    private void deleteExpiredVerificationCodes() {
        telegramVerificationCodeRepository.deleteAllBySentAtBefore(LocalDateTime.now().minusMinutes(1));
    }
}