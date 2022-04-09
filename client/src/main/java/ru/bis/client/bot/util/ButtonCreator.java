package ru.bis.client.bot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.bis.client.bot.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ButtonCreator {

    public static InlineKeyboardMarkup create(List<Callback> buttons) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne =
        buttons.stream().map(c -> createInlineKeyboardButton(c.getTitle(), c) )
                        .collect(Collectors.toList());

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

       return inlineKeyboardMarkup;
    }

    public static InlineKeyboardButton createInlineKeyboardButton(String text, Callback callback) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callback.name());
        return inlineKeyboardButton;
    }

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(List<Callback> callbacks) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (Callback callback : callbacks) {
            KeyboardRow row = new KeyboardRow();
            row.add(callback.getTitle());
            keyboard.add(row);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}
