package ru.bis.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bis.server.model.LinkTemplate;
import ru.bis.server.model.User;

import java.util.List;
import java.util.Optional;

public interface LinkTemplateRepo extends JpaRepository<LinkTemplate, Long> {

    LinkTemplate getByUser(User byId);
}

