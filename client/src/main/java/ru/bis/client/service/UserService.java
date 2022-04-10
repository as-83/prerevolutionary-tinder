package ru.bis.client.service;

import org.springframework.stereotype.Service;
import ru.bis.client.bot.BotState;
import ru.bis.client.http.Requester;
import ru.bis.client.model.FanStatus;
import ru.bis.client.model.Gender;
import ru.bis.client.model.User;
import ru.bis.client.model.UserAndStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    Map<Long, User> usersCache = new HashMap<>();
    Map<Long, List<User>> favorites = new HashMap<>();
    private final Map<Long, List<User>> fans = new ConcurrentHashMap<>();
    private final List<UserAndStatus> fansAndFavorites = new ArrayList<>();
    private final Requester requester;
    private final Map<Long, Boolean> fansAndFavsChanged = new HashMap<>();

    public UserService(Requester requester) {
        this.requester = requester;
    }

    public User getUserByTgId(long userTgId) {

        if(usersCache.containsKey(userTgId)) {
            return usersCache.get(userTgId);
        }

        Optional<User> optionalUser = requester.getUserByTgId(userTgId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBotState(BotState.SIGNUP);
            return optionalUser.get();
        }

        return new User( userTgId,  BotState.START);
    }

    public User save(User user) {
        if (user.getBotState().equals(BotState.SIGNUP)) {
            user = requester.registerNewUser(user);
        }
        usersCache.put(user.getTgId(), user);
        return usersCache.get(user.getTgId());
    }

    public void remove(User user) {
        usersCache.remove(user.getTgId());
    }

    public List<User> getUserFavoritesByTgId(Long tgId) {
        if (!favorites.containsKey(tgId)) {
            List<User> users = requester.getFavorites(tgId);
            favorites.put(tgId, users);
        }
        return favorites.get(tgId);
    }

    public List<User> getUserFansByTgId(Long tgId) {
        if (!fans.containsKey(tgId)) {
            List<User> users = requester.getFans(tgId);
            fans.put(tgId, users);
        }
        return fans.get(tgId);
    }

    public List<User> getCandidates(long tgId) {
        return requester.getCandidates(tgId);
    }

    public void addFavorite(long currentUserTgId, User user) {
        requester.sendLikeRequest(currentUserTgId, user.getTgId());
        updateFansAndFavorites(currentUserTgId, user.getTgId());
        fansAndFavsChanged.put(currentUserTgId, true);

    }

    private void updateFansAndFavorites(long fanTgId, long favoriteTgId) {
        fans.put(favoriteTgId, requester.getFans(favoriteTgId));
        favorites.put(fanTgId, requester.getFavorites(fanTgId));
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

        fansAndFavsChanged.put(tgId, false);
        return userAndStatuses;
    }

    private List<User> getUserFavorites(Long tgId) {
        if (!favorites.containsKey(tgId) ) {
            favorites.put(tgId, getUserFavoritesByTgId(tgId));
        }
        return favorites.get(tgId);
    }

    private List<User> getUserFans(Long tgId) {
        if (!fans.containsKey(tgId) ) {
            fans.put(tgId, getUserFansByTgId(tgId));
        }
        return fans.get(tgId);
    }

    public boolean isFansAndFavsChanged(long tgId) {
        return fansAndFavsChanged.get(tgId);
    }
}
