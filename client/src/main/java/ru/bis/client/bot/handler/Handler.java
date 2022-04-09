package ru.bis.client.bot.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.model.User;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);
    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    BotState operatedBotState();
    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<String> operatedCallBackQuery();
}
