package ru.bis.server.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bis.server.model.LinkTemplate;
import ru.bis.server.model.Sex;
import ru.bis.server.model.User;
import ru.bis.server.repo.LinkTemplateRepo;
import ru.bis.server.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final LinkTemplateRepo linkTemplateRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, LinkTemplateRepo linkTemplateRepo) {
        this.userRepo = userRepo;
        this.linkTemplateRepo = linkTemplateRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<User> getUsersFavorites(long id) {
        User user = userRepo.findById(id).orElse(new User());
        return user.getFavorites();
    }

    @Override
    public List<User> getUsersFans(long id) {
        User user = userRepo.findById(id).orElse(new User());
        return user.getFans();
    }

    @Override
    public Optional<User> getUserByTelegramId(long id) {
        return userRepo.getByTelegramId(id);
    }

    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> searchUsers(long searcherId) {
        User currentUser = userRepo.getById(searcherId);
        return userRepo.findAll().stream()
                .filter(u -> u.getUserId() != searcherId)
                .filter(u -> u.getFans().stream().noneMatch(f -> f.getUserId() == searcherId))
                .filter(u -> u.getSearchPreferences().equals(currentUser.getSex()) || u.getSearchPreferences().equals(Sex.ALL))
                .filter(u -> u.getSex().equals(currentUser.getSearchPreferences()) || currentUser.getSearchPreferences().equals(Sex.ALL))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addFavorite(long userId, long favoriteId) {
        User user = userRepo.getById(userId);
        User favorite = userRepo.getById(favoriteId);
        user.getFavorites().add(favorite);

        return userRepo.save(user) != null;
    }

    @Override
    public List<User> getAllUsers2() {

       long idCursor = 0L;
        while (true) {
            System.out.println("first id = " + idCursor);
            List<User> usersDb = userRepo.findTop50ByUserIdAfter(idCursor);
            System.out.printf("");
            if (usersDb.size() == 0) {
                System.out.println("-----Finish-----");
                break;
            }
            idCursor = usersDb.get(usersDb.size() - 1).getUserId();

            userRepo.deleteAll(usersDb.stream().filter(u -> ((u.getUserId() % 100) > 30)).collect(Collectors.toList()));

        }
        return userRepo.findAll();
    }

    @Override
    public void generate200() {
        List<LinkTemplate> users = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            User user = new User(100L + i, "name" + i, "description" + i, Sex.MAIL, Sex.FEMALE);
            LinkTemplate linkTemplate = new LinkTemplate(user);
            users.add(linkTemplate);
        }
         linkTemplateRepo.saveAll(users);

    }

    @Override
    public void deleteUser(long id) {
        User byId = userRepo.getById(id);
        LinkTemplate linkTemplate = linkTemplateRepo.getByUser(byId);
        linkTemplateRepo.delete(linkTemplate);
    }


}
