package ru.bis.client.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.UsersCache;
import ru.bis.client.bot.Callback;
import ru.bis.client.model.User;
import ru.bis.client.service.ImageService;
import ru.bis.client.service.UserService;
import ru.bis.client.bot.util.ButtonCreator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Component
public class MainPaige implements Handler {

    public static final String COMMAND_ORDER_ERROR_MESSAGE = "Данная команда не уместна в данный моментъ!";
    private final UserService userService;
    private final UsersCache usersCache;
    private final ImageService imageService;

    public MainPaige(UserService userService, UsersCache usersCache, ImageService imageService) {
        this.userService = userService;
        this.usersCache = usersCache;
        this.imageService = imageService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        String messageText = COMMAND_ORDER_ERROR_MESSAGE;
        SendMessage userProfile = createMessageTemplate(user);

        List<PartialBotApiMethod<? extends Serializable>> messagesList = new ArrayList<>();
        messagesList.add(userProfile);

        if (BotState.SIGNUP == user.getBotState()) {
            messageText = user.toString();

            String imageLocation = imageService.getImage(user.getDescription());
            SendPhoto sendPhoto = new SendPhoto(String.valueOf(user.getTgId()), new InputFile(new File(imageLocation)));
            sendPhoto.setCaption(user.getGender().getTitle() + ", " + user.getName());
            messagesList.clear();
            messagesList.add(sendPhoto);
            List<Callback> callbacks = List.of(Callback.FAVORITES, Callback.CANDIDATES);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);
            sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        }

        userProfile.setText(messageText);
        return messagesList;
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.SUBSCRIBE.name(), Callback.MENU.name());
    }

}
