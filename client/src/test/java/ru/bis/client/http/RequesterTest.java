package ru.bis.client.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bis.client.bot.BotState;
import ru.bis.client.model.Gender;
import ru.bis.client.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

class RequesterTest {
    private static Requester requester;

    @BeforeAll
    public static void init() {
        WebClient webClient  = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .defaultCookie("Key", "Value")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080/"))
                .build();
        requester = new Requester(webClient);
    }

    @Test
    public void getUserFavoritesReturnsListThenUserHasFavorites() {
        List<User> fans =  requester.getFavorites(1);
        assertThat(fans).isNotEmpty();
    }

    @Test
    public void getUserFavoritesReturnsEmptyListThenUserHasNotFavorites() {
        List<User> fans =  requester.getFavorites(2);
        assertThat(fans).isEmpty();
    }

    @Test
    public void getUserFansReturnsListThenUserHasFans() {
        List<User> fans =  requester.getFans(1);
        assertThat(fans).isNotEmpty();
    }

    @Test
    public void getUserFansReturnsEmptyListThenUserHasNotFans() {
        List<User> fans =  requester.getFans(6);
        assertThat(fans).isEmpty();
    }


    @Test
    void getUserByTgIdReturnsUserThenExists() {
        Optional<User> optionalUser = requester.getUserByTgId(2L);
        assertThat(optionalUser.get()).hasFieldOrPropertyWithValue("tgId", 2L);
    }

    @Test
    void getUserByTgIdReturnsEmptyThenNotExists() {
        Optional<User> optionalUser = requester.getUserByTgId(2222L);
        assertThat(optionalUser.isPresent()).isFalse();
    }

    @Test
    void addUserReturnsUserWithDbId() {
        User newUser = new User(15, "Герасим", "Ищу собачку, которая не тонет", Gender.MAIL, Gender.ALL, BotState.SIGNUP);
        User user = requester.registerNewUser(newUser);
        assertThat(user.getDbId()).isGreaterThan(10);
    }

    @Test
    void sendLikeRequestReturnsTrueIfLikeSaved() {
        Optional<Boolean> isLikeSaved = requester.sendLikeRequest(3, 4);

        assertThat(isLikeSaved.isPresent()).isTrue();
        assertThat(isLikeSaved.get()).isTrue();
    }
}