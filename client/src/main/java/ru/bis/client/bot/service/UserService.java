package ru.bis.client.bot.service;

import org.springframework.stereotype.Service;
import ru.bis.client.bot.BotState;
import ru.bis.client.bot.model.Gender;
import ru.bis.client.bot.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    Map<Long, User> users = new HashMap<>();
    private int favoriteCursor = 0;


    public User getUserById(long userTgId) {
        if (!users.containsKey(userTgId)) {
            users.put(userTgId, new User(users.size() + 1, userTgId, "New User", "User description", Gender.MAIL, Gender.MAIL, BotState.START));
        }

        return users.get(userTgId);//TODO getting from rest service
    }

    public void save(User user) {
        users.put(user.getTgId(), user);
    }

    public void remove(User user) {
        users.remove(user.getTgId());
    }

    public User getFansAndFavorites(User user, String message) {
        return null;
    }


    public List<User> getUserFavoritesByTgId(Long tgId) {
        return Collections.emptyList();
    }

    public List<User> getUserFansByTgId(Long tgId) {

        return Collections.emptyList();
    }
}
