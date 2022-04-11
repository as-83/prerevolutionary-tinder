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
import ru.bis.client.model.FanStatus;
import ru.bis.client.model.Gender;
import ru.bis.client.model.User;
import ru.bis.client.model.UserAndStatus;
import ru.bis.client.service.ImageService;
import ru.bis.client.service.ModernToSlavishTranslator;
import ru.bis.client.service.UserService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.bis.client.bot.handler.MainPaige.IMAGE_CREATION_ERROR;
import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Slf4j
@Component
public class FavoritesHandler implements Handler {

    private static final String NO_FAVORITES = "У Васъ н\u0462тъ любимцевъ";
    private final ImageService imageService;
    private final UserService userService;
    private final ModernToSlavishTranslator modernToSlavishTranslator;
    private static int favoriteIndex = 0;//TODO

    private List<UserAndStatus> fansAndFavorites = new ArrayList<>();//TODO

    public FavoritesHandler(ImageService imageService, UserService userService, ModernToSlavishTranslator modernToSlavishTranslator) {
        this.imageService = imageService;
        this.userService = userService;
        this.modernToSlavishTranslator = modernToSlavishTranslator;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage sendMessage = createMessageTemplate(user);
        List<PartialBotApiMethod<? extends Serializable>> sendMessages = new ArrayList<>();
        sendMessages.add(sendMessage);

        if (user.getBotState() == BotState.SIGNUP) {

            if (fansAndFavorites.isEmpty() || userService.isFansAndFavsChanged(user.getTgId())) {//TODO update fansAndFavorites if changed
                fansAndFavorites = userService.getFansAndFavorites(user.getTgId());
            }

            if (fansAndFavorites.isEmpty()) {
                sendMessage.setText(NO_FAVORITES);
                return sendMessages;
            }

            handleCallback(message);
            UserAndStatus userAndStatus = fansAndFavorites.get(favoriteIndex);
            String translatedDescription = modernToSlavishTranslator.translate(userAndStatus.getUser().getDescription());
            File imageFile = imageService.getImage(translatedDescription);
            SendPhoto photoMessage = new SendPhoto(String.valueOf(user.getTgId()), new InputFile(imageFile));
            if (imageFile == null) {
                sendMessage.setText(IMAGE_CREATION_ERROR);
                return sendMessages;
            }

            photoMessage.setCaption(createCaption(userAndStatus));

            List<Callback> callbacks = List.of(Callback.PREVIOUS, Callback.MENU, Callback.NEXT);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);

            photoMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessages.clear();
            sendMessages.add(photoMessage);
        }

        return sendMessages;
    }

    private String createCaption(UserAndStatus userAndStatus) {
        String status = userAndStatus.getStatus().getTitle();
        User user = userAndStatus.getUser();

        if (userAndStatus.getStatus() == FanStatus.FAVORITE && user.getGender() == Gender.FEMALE) {
            status = "Любима Вами";
        }
        return user.getGender().getTitle() + ", " + user.getName() + ". " + status;

    }

    private void handleCallback(String message) {
        int maxIndex = fansAndFavorites.size();
        if (Callback.NEXT.name().equals(message)) {
            favoriteIndex++;
        }
        if (Callback.PREVIOUS.name().equals(message)) {
            favoriteIndex--;
        }
        if (favoriteIndex == maxIndex) {
            favoriteIndex = 0;
        }
        if (favoriteIndex == -1) {
            favoriteIndex = maxIndex - 1;
        }
    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(
                Callback.FAVORITES.name(),
                Callback.NEXT.name(),
                Callback.PREVIOUS.name());
    }
}
