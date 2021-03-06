package ru.bis.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bis.server.model.User;
import ru.bis.server.servise.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users/{telegramId}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable long telegramId) {
        Optional<User> user = service.getUserByTelegramId(telegramId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user/*, @PathVariable long id*/) {

        HttpStatus httpStatus = HttpStatus.CREATED;
        User newUser = service.addUser(user);
        if (newUser == null) {
            newUser = new User();
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(newUser, httpStatus);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/candidates")
    public ResponseEntity<List<User>> getCandidates(@PathVariable long id) {
        List<User> users = service.searchUsers(id);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/favorites/{id}")
    public ResponseEntity<List<User>> getUserFavorites(@PathVariable long id) {
        List<User> favorites = service.getUsersFavorites(id);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @GetMapping("/fans/{id}")
    public ResponseEntity<List<User>> getUserFans(@PathVariable long id) {
        List<User> fans = service.getUsersFans(id);
        return new ResponseEntity<>(fans, HttpStatus.OK);
    }

    @PostMapping("/users/favorite")
    public ResponseEntity<Boolean> addFavorite(@RequestParam long userId,
                                            @RequestParam long favoriteId) {
        HttpStatus httpStatus = HttpStatus.OK;
        boolean result = true;

        if (!service.addFavorite(userId, favoriteId)) {
            httpStatus = HttpStatus.BAD_REQUEST;
            result = false;
        }
        return new ResponseEntity<>(result, httpStatus);
    }


}
