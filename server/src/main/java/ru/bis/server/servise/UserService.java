package ru.bis.server.servise;

import ru.bis.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    List<User> getUsersFavorites(long id);

    List<User> getUsersFans(long id);

    Optional<User> getUserByTelegramId(long id);

    User addUser(User user);

    List<User> searchUsers(long searcherId);

    boolean addFavorite(long userId, long favoriteId);

    List<User> getAllUsers2();

    void generate200();

    void deleteUser(long id);
}
