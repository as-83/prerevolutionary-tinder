package ru.bis.client.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bis.client.bot.BotState;
import ru.bis.client.model.User;
import ru.bis.client.service.UserService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
@Slf4j
@Component
public class HandlersDispatcher {
    // Храним доступные хендлеры в списке
    private final List<Handler> handlers;
    private final IncorrectMessageHandler incorrectMessageHandler;

    private final UserService userService;

    public HandlersDispatcher(List<Handler> handlers, IncorrectMessageHandler incorrectMessageHandler, UserService userService) {
        this.handlers = handlers;
        this.incorrectMessageHandler = incorrectMessageHandler;
        this.userService = userService;
    }


    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                final long userTgId = message.getFrom().getId();
                log.info(message.getText() + " from user " + message.getFrom().getUserName());
                final User user = userService.getUserById(userTgId);//TODO


                return getHandlerByState(user.getBotState()).handle(user, message.getText());

            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final long userTgId = callbackQuery.getFrom().getId();
                final User user = userService.getUserById(userTgId);//TODO
                log.info(callbackQuery.getData() + " from user " + callbackQuery.getFrom().getUserName());
                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(user, callbackQuery.getData());
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(BotState botState) {

        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(botState))
                .findAny()
                .orElse(incorrectMessageHandler);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(q -> q.equals(query)))
                .findAny()
                .orElse(incorrectMessageHandler);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
