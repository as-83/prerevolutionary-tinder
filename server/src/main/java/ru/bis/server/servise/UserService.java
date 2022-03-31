package ru.bis.server.servise;

import ru.bis.server.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    List<User> getUsersFavorites(long id);

    List<User> getUsersFans(long id);

    Optional<User> getUserBuId(long id);

    User addUser(User user);
}
