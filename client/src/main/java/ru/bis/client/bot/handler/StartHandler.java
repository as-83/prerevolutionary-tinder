package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.model.User;
import ru.bis.client.bot.service.UserService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class StartHandler implements Handler{

    private final UserService userService;

    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage welcomeMessage = createMessageTemplate(user);
        welcomeMessage.setText("Васъ приветствует ботъ ....");//TODO

        SendMessage registrationMessage = createMessageTemplate(user);
        registrationMessage.setText("Какъ Васъ величать!");

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
        return Collections.emptyList();
    }
}
