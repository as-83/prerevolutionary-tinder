package ru.bis.client.bot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bis.client.bot.Callback;
import ru.bis.client.model.User;

public class MessageCreator {
    public static SendMessage createMessageTemplate(User user) {
        return createMessageTemplate(String.valueOf(user.getTgId()));
    }

    // Создаем шаблон SendMessage с включенным Markdown
    public static SendMessage createMessageTemplate(String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        return sendMessage;
    }

    // Создаем кнопку
    public static InlineKeyboardButton createInlineKeyboardButton(String text, Callback callback) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callback.name());
        return inlineKeyboardButton;
    }
}
