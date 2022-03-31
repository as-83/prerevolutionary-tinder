package ru.bis.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bis.server.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {

}

