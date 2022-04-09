package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.Callback;
import ru.bis.client.model.User;
import ru.bis.client.service.UserService;
import ru.bis.client.bot.util.ButtonCreator;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.bis.client.bot.handler.MainPaige.COMMAND_ORDER_ERROR_MESSAGE;
import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class NameHandler implements Handler {


    public static final String GENDER_QUESTION = ". Вы сударь или сударыня?";
    private final UserService userService;

    public NameHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        SendMessage sendMessage = createMessageTemplate(user);
        String messageText = COMMAND_ORDER_ERROR_MESSAGE;
        if (user.getBotState() == operatedBotState()) {
            user.setBotState(BotState.GENDER);
            user.setName(message);
            userService.save(user);
            // кнопки
            List<Callback> callbacks = List.of(Callback.MAIL, Callback.FEMALE);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            messageText = message + GENDER_QUESTION;
        }

        sendMessage.setText(messageText);
        return List.of(sendMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NAME;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
