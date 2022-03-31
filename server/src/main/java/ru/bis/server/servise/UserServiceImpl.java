package ru.bis.server.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bis.server.domain.User;
import ru.bis.server.repo.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<User> getUsersFavorites(long id) {
        User user =  userRepo.findById(id).orElse(new User());
        return user.getFavorites();
    }

    @Override
    public List<User> getUsersFans(long id) {
        User user =  userRepo.findById(id).orElse(new User());
        return user.getFans();
    }

    @Override
    public Optional<User> getUserBuId(long id) {
        return userRepo.findById(id);
    }

    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }


}
