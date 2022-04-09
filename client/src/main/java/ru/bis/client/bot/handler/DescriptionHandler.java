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
public class DescriptionHandler implements Handler {

    public static final String PREFERENCE_MESSAGE = ". Кого вы ищете?";
    private final UserService userService;

    public DescriptionHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        String messageText = COMMAND_ORDER_ERROR_MESSAGE;
        SendMessage messageTemplate = createMessageTemplate(user);

        if (user.getBotState() == operatedBotState()) {
            user.setBotState(BotState.PREFERENCES);
            user.setDescription(message);
            userService.save(user);
            messageText = user.getGender().getTitle() + PREFERENCE_MESSAGE;
            List<Callback> callbacks = List.of(Callback.MAIL_SEARCH, Callback.FEMALE_SEARCH, Callback.ALL_SEARCH);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);
            messageTemplate.setReplyMarkup(inlineKeyboardMarkup);
        }

        messageTemplate.setText(messageText);
        return List.of(messageTemplate);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.DESCRIPTION;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
