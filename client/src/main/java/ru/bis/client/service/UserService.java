package ru.bis.client.service;

import org.springframework.stereotype.Service;
import ru.bis.client.bot.BotState;
import ru.bis.client.model.Gender;
import ru.bis.client.model.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    Map<Long, User> users = new HashMap<>();
    Map<Long, List<User>> favorites = new HashMap<>();
    Map<Long, Boolean> favoritesChanged = new HashMap<>();

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
        if(!favorites.containsKey(tgId)) {
            return Collections.emptyList();
        }
        favoritesChanged.put(tgId, false);
        return favorites.get(tgId);
    }

    public List<User> getUserFansByTgId(Long tgId) {

        return Collections.emptyList();
    }

    public List<User> getCandidates(long tgId) {
        return Stream.iterate(1, i -> i + 1)
                .limit(10)
                .map(i -> new User(i, i, "User" + i, "Description of user" + i, Gender.FEMALE, Gender.ALL, BotState.SIGNUP ))
                .collect(Collectors.toList());
    }

    public void addFavorite(long currentUserTgId, User user) {
        if (!favorites.containsKey(currentUserTgId)) {
            List<User> usersFavorites = new ArrayList<>();
            favorites.put(currentUserTgId, usersFavorites);
        }
        favoritesChanged.put(currentUserTgId, true);
        favorites.get(currentUserTgId).add(user);
    }

    public boolean favoritesChanged(long tgId) {
        return favoritesChanged.get(tgId);
    }
}
