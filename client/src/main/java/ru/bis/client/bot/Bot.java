package ru.bis.client.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bis.client.bot.handler.HandlersDispatcher;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    @Getter
    private String botUsername;

    @Value("${bot.token}")
    @Getter
    private String botToken;

    private final HandlersDispatcher handlersDispatcher;

    public Bot(HandlersDispatcher handlersDispatcher) {
        this.handlersDispatcher = handlersDispatcher;
    }

    public void onUpdateReceived(Update update) {
        log.debug("New Update");
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = handlersDispatcher.handle(update);
        if (messagesToSend != null && !messagesToSend.isEmpty()) {
            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {
                    executeWithExceptionCheck((SendMessage) response);
                }
                if (response instanceof SendPhoto) {
                    executeImgWithExceptionCheck((SendPhoto) response);
                }
            });
        }
    }

    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            log.info(sendMessage.getText());
            execute(sendMessage);
        } catch (TelegramApiException e) {

            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeImgWithExceptionCheck(SendPhoto sendMessage) {
        try {
            log.info(sendMessage.getPhoto().getMediaName());
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
