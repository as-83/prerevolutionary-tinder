package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.model.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component("incorrectMessage")
public class IncorrectMessageHandler implements Handler {
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText("Некорректный запрос!");
        return List.of(sendMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
