package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.model.Callback;
import ru.bis.client.bot.model.Gender;
import ru.bis.client.bot.model.User;
import ru.bis.client.bot.service.UserService;
import ru.bis.client.bot.util.ButtonCreator;

import java.io.Serializable;
import java.util.List;

import static ru.bis.client.bot.handler.MainPaige.COMMAND_ORDER_ERROR_MESSAGE;
import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class SearchPreferencesHandler implements Handler {
    public static final String SUBSCRIPTION_MESSAGE = ", изволите зарегистрироваться?";


    private final UserService userService;

    public SearchPreferencesHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage sendMessage = createMessageTemplate(user);
        String messageText = COMMAND_ORDER_ERROR_MESSAGE;

        if (user.getBotState().equals(BotState.PREFERENCES)) {
            String substring = message.substring(0, message.indexOf("_"));
            Gender gender = Gender.valueOf(substring);
            user.setSearchGender(gender);
            List<Callback> callbacks = List.of(Callback.SUBSCRIBE, Callback.UNSUBSCRIBE);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            messageText = user.getGender().getTitle() + SUBSCRIPTION_MESSAGE;
            user.setBotState(BotState.SIGNUP);
            userService.save(user);
        }

        sendMessage.setText(messageText);
        return List.of(sendMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.MAIL_SEARCH.name(), Callback.FEMALE_SEARCH.name(), Callback.ALL_SEARCH.name());
    }
}
