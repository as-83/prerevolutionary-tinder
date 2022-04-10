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
import ru.bis.client.service.UserService;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.bis.client.bot.util.MessageCreator.createMessageTemplate;

@Slf4j
@Component
public class CandidatesHandler implements Handler {

    private static final String NO_CANDIDATES = "Подходящихъ кандадатуръ н\u0462тъ";
    private final ImageService imageService;
    private final UserService userService;
    private static int candidateIndex = 0;

    private List<User> candidates = new ArrayList<>();

    public CandidatesHandler(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        SendMessage sendMessage = createMessageTemplate(user);
        List<PartialBotApiMethod<? extends Serializable>> sendMessages = new ArrayList<>();
        sendMessages.add(sendMessage);

        if (user.getBotState() == BotState.SIGNUP) {
            handleCommand(user, message);
            if (candidates.isEmpty()) {
                candidates = userService.getCandidates(user.getTgId());
            }
            if (candidates.isEmpty()) {
                sendMessage.setText(NO_CANDIDATES);
                return sendMessages;
            }

            User candidate = candidates.get(candidateIndex);

            String imageLocation = imageService.getImage(candidate.getDescription());
            SendPhoto photoMessage = new SendPhoto(String.valueOf(user.getTgId()), new InputFile(new File(imageLocation)));

            photoMessage.setCaption(candidate.getGender().getTitle()
                    + ", " + candidate.getName());

            List<Callback> callbacks = List.of(Callback.PREV_CANDIDATE, Callback.MENU, Callback.NEXT_CANDIDATE);
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonCreator.create(callbacks);

            photoMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessages.clear();
            sendMessages.add(photoMessage);
        }

        return sendMessages;
    }

    private void handleCommand(User currentUser, String message) {

        if (Callback.NEXT_CANDIDATE.name().equals(message)) {
            userService.addFavorite(currentUser.getTgId(), candidates.get(candidateIndex));
            candidates.remove(candidateIndex);
        }

        if (Callback.PREV_CANDIDATE.name().equals(message)) {
            candidateIndex++;
        }

        if (candidateIndex == candidates.size()) {
            candidateIndex = 0;
        }

    }

    @Override
    public BotState operatedBotState() {
        return BotState.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(Callback.CANDIDATES.name(), Callback.NEXT_CANDIDATE.name(), Callback.PREV_CANDIDATE.name());
    }
}
