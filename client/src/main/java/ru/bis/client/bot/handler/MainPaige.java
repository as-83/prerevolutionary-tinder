package ru.bis.client.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.Callback;
import ru.bis.client.bot.util.ButtonCreator;
import ru.bis.client.model.User;
import ru.bis.client.service.ImageService;
import ru.bis.client.service.ModernToSlavishTranslator;
import ru.bis.client.service.UserService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Slf4j
@Component
public class MainPaige implements Handler {

    public static final String COMMAND_ORDER_ERROR_MESSAGE = "Данная команда не уместна в данный моментъ!";
    public static final String IMAGE_CREATION_ERROR = "Что-то пошло не такъ. Попробуйте позже";
    private final UserService userService;
    private final ModernToSlavishTranslator modernToSlavishTranslator;
    private final ImageService imageService;

    public MainPaige(UserService userService, ModernToSlavishTranslator modernToSlavishTranslator, ImageService imageService) {
        this.userService = userService;
        this.modernToSlavishTranslator = modernToSlavishTranslator;
        this.imageService = imageService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage userProfile = createMessageTemplate(user);

        List<PartialBotApiMethod<? extends Serializable>> messagesList = new ArrayList<>();
        messagesList.add(userProfile);

        if (BotState.TRY_TO_SIGN == user.getBotState()) {
            user.setBotState(BotState.SIGNUP);
            userService.save(user);
        }

        if (BotState.SIGNUP == user.getBotState()) {
            String translatedDescription = modernToSlavishTranslator.translate(user.getDescription());
            File imageLocation = imageService.getImage(translatedDescription);
            if (imageLocation == null) {
                userProfile.setText(IMAGE_CREATION_ERROR);
                return messagesList;
            }
            SendPhoto sendPhoto = new SendPhoto(String.valueOf(user.getTgId()), new InputFile(imageLocation));
            sendPhoto.setCaption(user.getGender().getTitle() + ", " + user.getName());
            messagesList.clear();
            messagesList.add(sendPhoto);
            List<Callback> callbacks = List.of(Callback.FAVORITES, Callback.EDIT, Callback.CANDIDATES);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);
            sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        }
        return messagesList;
    }

    @Override
    public BotState operatedBotState() {
        return BotState.SIGNUP;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.SUBSCRIBE.name(), Callback.MENU.name());
    }

}
