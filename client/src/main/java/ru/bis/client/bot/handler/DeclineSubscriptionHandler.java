package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.Callback;
import ru.bis.client.model.User;
import ru.bis.client.service.UserService;

import java.io.Serializable;
import java.util.List;

import static ru.bis.client.bot.handler.MainPaige.COMMAND_ORDER_ERROR_MESSAGE;
import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class DeclineSubscriptionHandler implements Handler {

    public static final String DECLINE_SUBSCRIPTION_MESSAGE = "Регистрация отменена. Чтоб начать заново нажмите /start";
    private final UserService userService;

    public DeclineSubscriptionHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        String messageText = COMMAND_ORDER_ERROR_MESSAGE;

        if (BotState.SIGNUP == user.getBotState()) {
            user.setBotState(BotState.START);
            userService.remove(user);
            messageText = user.getGender().getTitle() + DECLINE_SUBSCRIPTION_MESSAGE;
        }

        SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(messageText);

        return List.of(sendMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.UNSUBSCRIBE.name());
    }
}
