package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.model.Callback;
import ru.bis.client.bot.model.Gender;
import ru.bis.client.bot.model.User;
import ru.bis.client.bot.service.UserService;

import java.io.Serializable;
import java.util.List;

import static ru.bis.client.bot.handler.MainPaige.COMMAND_ORDER_ERROR_MESSAGE;
import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class GenderHandler implements Handler {


    public static final String DESCRIPTION_MESSAGE = "! Опишите себя";
    private final UserService userService;

    public GenderHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        String messageText = COMMAND_ORDER_ERROR_MESSAGE;

        if (user.getBotState() == BotState.GENDER) {
            Gender gender = Gender.valueOf(message);
            user.setGender(gender);
            user.setBotState(BotState.DESCRIPTION);
            userService.save(user);
            messageText = user.getGender().getTitle() + DESCRIPTION_MESSAGE;
        }

        SendMessage welcomeMessage = createMessageTemplate(user);
        welcomeMessage.setText(messageText);

        return List.of(welcomeMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.MAIL.name(),
                        Callback.FEMALE.name());
    }
}
