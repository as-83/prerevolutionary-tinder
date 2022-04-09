package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bis.client.bot.BotState;
import ru.bis.client.model.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class HelpHandler implements Handler {

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {


        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText(String.format("" +
                "You've asked for help %s? Here it comes!", user.getName()));
        return List.of(messageTemplate);

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
