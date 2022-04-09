package ru.bis.client.bot;

import org.springframework.stereotype.Component;
import ru.bis.client.bot.model.User;
import ru.bis.client.bot.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class UsersCache {

    private final UserService userService;
    private Map<Long, List<User>> usersFavorites = new ConcurrentHashMap<>();
    private Map<Long, List<User>> usersFans = new ConcurrentHashMap<>();
    List<Map<User, String>> fansAndFavorites = new ArrayList<>();

    private Map<Long, Integer> favoritesIteratorPosition = new ConcurrentHashMap<>();

    public UsersCache(UserService userService) {
        this.userService = userService;
    }

    public List<Map<User, String>>  getFansAndFavorites(Long tgId) {

        List<Map<User, String>> listMaps = new ArrayList<>();
        List<User> userFavorites = getUserFavorites(tgId);
        List<User> userFans = getUserFans(tgId);
        List<User> lovingEachOver = userFavorites.stream().filter(f -> userFans.stream().anyMatch(uf -> uf.getDbId() == f.getDbId()))
                .collect(Collectors.toList());

        userFavorites.stream()
                .filter(f -> lovingEachOver.stream().noneMatch(uf -> uf.getDbId() == f.getDbId()))
                .forEach(e -> listMaps.add((Map<User, String>) new HashMap<>().put(e, "Любимцы")));

        userFans.stream()
                .filter(f -> lovingEachOver.stream().noneMatch(uf -> uf.getDbId() == f.getDbId()))
                .forEach(e -> listMaps.add((Map<User, String>) new HashMap<>().put(e, "Вы любимы")));

        lovingEachOver .forEach(e -> listMaps.add((Map<User, String>) new HashMap<>().put(e, "Взаимность")));

        return listMaps;
    }



    private List<User> getUserFavorites(Long tgId) {
        if (!usersFavorites.containsKey(tgId) ) {
            usersFavorites.put(tgId, userService.getUserFavoritesByTgId(tgId));
        }
        return usersFavorites.get(tgId);
    }

    private List<User> getUserFans(Long tgId) {
        if (!usersFans.containsKey(tgId) ) {
            usersFans.put(tgId, userService.getUserFansByTgId(tgId));
        }
        return usersFans.get(tgId);
    }

}
