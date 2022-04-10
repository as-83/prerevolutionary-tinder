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
import ru.bis.client.model.User;
import ru.bis.client.model.UserAndStatus;
import ru.bis.client.service.ImageService;
import ru.bis.client.service.UserService;
import ru.bis.client.bot.util.ButtonCreator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Slf4j
@Component
public class FavoritesHandler implements Handler {

    private static final String INCORRECT_COMMAND_MESSAGE = "Неверная команда!";
    private static final String NO_FAVORITES = "У Васъ н\u0462тъ любимцевъ";
    private final ImageService imageService;
    private final UserService userService;
    private static int favoriteIndex = 0;//TODO

    private List<UserAndStatus> fansAndFavorites = new ArrayList<>();//TODO

    public FavoritesHandler(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
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

            String imageLocation = imageService.getImage(userAndStatus.getUser().getDescription());
            SendPhoto photoMessage = new SendPhoto(String.valueOf(user.getTgId()), new InputFile(new File(imageLocation)));

            photoMessage.setCaption(userAndStatus.getUser().getGender().getTitle()
                    + ", " + userAndStatus.getStatus().getTitle());

            List<Callback> callbacks = List.of(Callback.PREVIOUS, Callback.MENU, Callback.NEXT);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);

            photoMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessages.clear();
            sendMessages.add(photoMessage);
        }

        return sendMessages;
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
        return List.of(Callback.FAVORITES.name(), Callback.NEXT.name(), Callback.PREVIOUS.name());
    }
}
