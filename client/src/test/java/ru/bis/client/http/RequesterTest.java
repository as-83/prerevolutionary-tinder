package ru.bis.client.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bis.client.model.User;

import java.util.Collections;
import java.util.List;

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
    public void getUserFavorites() {
        List<User> fans =  requester.getFavorites(1);
        assertThat(fans).isNotEmpty();
    }

    @Test
    public void getUserFans() {
        List<User> fans =  requester.getFans(1);
        assertThat(fans).isNotEmpty();
    }


}