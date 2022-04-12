package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.Callback;
import ru.bis.client.model.User;
import ru.bis.client.service.UserService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class StartHandler implements Handler{

    public static final String HELLO_MESSAGE = "Васъ приветствует ботъ ....";
    private static final String EDIT_PROFILE_MESSAGE = "Давайте изменимъ Ваши данные";
    public static final String NAME_ASKING_MESSAGE = "Какъ Васъ величать!";
    private final UserService userService;

    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage welcomeMessage = createMessageTemplate(user);
        String firstMessage = HELLO_MESSAGE;
        if (message.equals(Callback.EDIT.name())) {
            firstMessage = EDIT_PROFILE_MESSAGE;
        }
        welcomeMessage.setText(firstMessage);

        SendMessage registrationMessage = createMessageTemplate(user);
        registrationMessage.setText(NAME_ASKING_MESSAGE);

        user.setBotState(BotState.NAME);
        userService.save(user);

        return List.of(welcomeMessage, registrationMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.EDIT.name());
    }
}
