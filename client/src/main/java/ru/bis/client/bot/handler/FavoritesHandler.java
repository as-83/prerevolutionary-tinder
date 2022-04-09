package ru.bis.client.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.UsersCache;
import ru.bis.client.bot.model.Callback;
import ru.bis.client.bot.model.Gender;
import ru.bis.client.bot.model.User;
import ru.bis.client.bot.service.ImageService;
import ru.bis.client.bot.service.UserService;
import ru.bis.client.bot.util.ButtonCreator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Slf4j
@Component
public class FavoritesHandler implements Handler {

    private static final String INCORRECT_COMMAND_MESSAGE = "Неверная команда!";
    private final ImageService imageService;
    private final UserService userService;
    private final UsersCache usersCache;
    private static int favoriteCursor = 0;
    private static Map<Long, List<User>> usersFavorites= new ConcurrentHashMap<>();

    public FavoritesHandler(ImageService imageService, UserService userService, UsersCache usersCache) {
        this.imageService = imageService;
        this.userService = userService;
        this.usersCache = usersCache;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {


        String messageText = INCORRECT_COMMAND_MESSAGE;
        SendMessage sendMessage = createMessageTemplate(user);
        List<PartialBotApiMethod<? extends Serializable>> sendMessages = new ArrayList<>();
        sendMessages.add(sendMessage);

        log.info(message);
        if (user.getBotState() == BotState.SIGNUP) {
            //List<Map<User, String>> fansAndFavorites =
            User favorite = userService.getFansAndFavorites(user, message);
            //userService.save(user);
            switchFavorite(message);
            messageText = "Описание Любимца №" + favoriteCursor;
            String imageLocation = imageService.getImage(messageText);
            SendPhoto photoMessage = new SendPhoto(String.valueOf(user.getTgId()), new InputFile(new File(imageLocation)));
            photoMessage.setCaption(Gender.MAIL.getTitle() + ", " + "Любимец №" + favoriteCursor);

            List<Callback> callbacks = List.of(Callback.PREVIOUS, Callback.MENU, Callback.NEXT);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);
            // ReplyKeyboardMarkup replyKeyboardMarkup = ButtonCreator.createReply(callbacks);
            //sendMessage.setReplyMarkup(replyKeyboardMarkup);
            photoMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessages.clear();
            sendMessages.add(photoMessage);
        }

        sendMessage.setText(messageText);

        return sendMessages;
    }

    private void switchFavorite(String message) {
        if (Callback.NEXT.name().equals(message)) {
            favoriteCursor++;
        }
        if (Callback.PREVIOUS.name().equals(message)) {
            favoriteCursor--;
        }
        if (favoriteCursor == 11) {
            favoriteCursor = 1;
        }
        if (favoriteCursor == 0) {
            favoriteCursor = 10;
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
