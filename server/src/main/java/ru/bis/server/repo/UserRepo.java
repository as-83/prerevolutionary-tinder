package ru.bis.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bis.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> getByTelegramId(long id);
    List<User> findTop50ByUserIdAfter(Long userId);
}

