package ru.bis.client.bot;

import org.springframework.stereotype.Component;
import ru.bis.client.model.FanStatus;
import ru.bis.client.model.User;
import ru.bis.client.model.UserAndStatus;
import ru.bis.client.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class UsersCache {

    private final UserService userService;

    private Map<Long, List<User>> usersFavorites = new ConcurrentHashMap<>();
    private Map<Long, List<User>> usersFans = new ConcurrentHashMap<>();

    private Map<Long, Integer> favoritesIteratorPosition = new ConcurrentHashMap<>();

    public UsersCache(UserService userService) {
        this.userService = userService;
    }

    public List<UserAndStatus>  getFansAndFavorites(Long tgId) {

        List<UserAndStatus> userAndStatuses = new ArrayList<>();
        List<User> userFavorites = getUserFavorites(tgId);
        List<User> userFans = getUserFans(tgId);

        List<User> lovingEachOver = userFavorites.stream().filter(f -> userFans.stream().anyMatch(uf -> uf.getDbId() == f.getDbId()))
                .collect(Collectors.toList());

        userFavorites.stream()
                .filter(f -> lovingEachOver.stream().noneMatch(uf -> uf.getDbId() == f.getDbId()))
                .forEach(e -> userAndStatuses.add(new UserAndStatus(e, FanStatus.FAVORITE)));

        userFans.stream()
                .filter(f -> lovingEachOver.stream().noneMatch(uf -> uf.getDbId() == f.getDbId()))
                .forEach(e -> userAndStatuses.add(new UserAndStatus(e, FanStatus.FAN)));

        lovingEachOver .forEach(e -> userAndStatuses.add(new UserAndStatus(e, FanStatus.BOTH)));

        return userAndStatuses;
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
