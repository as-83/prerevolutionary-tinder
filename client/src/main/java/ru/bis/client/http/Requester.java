package ru.bis.client.http;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.bis.client.model.User;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.bis.client.http.UriStorage.*;

@Service
public class Requester {

    private final WebClient webClient;

    public Requester(WebClient webClient) {
        this.webClient = webClient;
    }


    /**
     * Получение списка пользователей, которым понравился текущий пользователь
     *
     * @param id - id  пользователя
     * @return списка пользователей, которым понравился пользователь
     */
    public List<User> getFans(long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_FANS_URI)
                        .build(id))
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();
    }

    /**
     * Получение списка понравившихся пользователю профилей
     *
     * @param id - id  пользователя
     * @return список понравившихся пользователю профилей
     */
    public List<User> getFavorites(long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_FAVORITES_URI)
                        .build(id))
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();
    }


    /**
     *
     *
     * @param
     * @return
     */
    public Optional<User> getUserByTgId(long id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_USER_BY_ID_URI)
                        .build(id))
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .blockOptional();
    }


    /**
     * Сохранение нового пользователя
     *
     * @param user - профиль нового пользователя
     * @return опционального пользователя при успешной регистрации или Optional.empty() при ошибке
     */
    public User registerNewUser(User user) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(MAIN_URI)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .block();
    }


    /**
     * Лайк
     *  @param usersIdFrom - id поставившего лайк
     * @param userIdTo - id кому поставили лайк
     * @return
     */
    public Optional<Boolean> sendLikeRequest(long usersIdFrom, long userIdTo) {
        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add("userId", String.valueOf(usersIdFrom));
        bodyValues.add("favoriteId", String.valueOf(userIdTo));
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(LIKE_URI)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromFormData(bodyValues))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 200 ? Mono.empty() : Mono.error(ex))
                .blockOptional();
    }

    /**
     * Обновление профиля пользователя
     *
     * @param user - профиль
     */
    public void updateUser(User user) {
        webClient.put()
                .uri(uriBuilder -> uriBuilder.path(MAIN_URI)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    /**
     * Запрос логина
     *
     * @param id - telegram id пользователя
     * @return  пользователя при успешном логине
     */
    public Optional<User> userLogin(long id) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(MAIN_URI)
                        .build())
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Mono.just(id), Long.class)
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .blockOptional();

    }

    public List<User> getCandidates(long tgId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_CANDIDATES_URI)
                        .build(tgId))
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();

    }
}
