package uz.academy.exam.Exam.service.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import uz.academy.exam.Exam.config.bot.BotConfiguration;
import uz.academy.exam.Exam.model.entity.TelegramUser;
import uz.academy.exam.Exam.service.base.TelegramUserService;
import uz.academy.exam.Exam.service.base.UserService;

import java.util.List;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfiguration botConfiguration;
    private final UserService userService;
    private final TelegramUserService telegramUserService;
    private final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);

    @Autowired
    public TelegramBot(BotConfiguration botConfiguration, UserService userService, TelegramUserService telegramUserService) {
        this.botConfiguration = botConfiguration;
        this.userService = userService;
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

        TelegramUser telegramUser = telegramUserService.findByChatId(chatId);

        if (telegramUser == null) {
            registerLogic(message, text, chatId);
            return;
        }

        sendTextMessage(chatId, text);
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
            sendTextMessage(chatId, "You have successfully registered in the bot!.\nUse /help command to get help.");

        } else
            sendTextMessage(chatId, "You are not registered in the bot!.\nUse /start command to register.");
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
    public void sendTextMessage(Long chatId, String text) {
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .build());
    }
}