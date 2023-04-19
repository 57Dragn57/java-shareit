package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "users"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getUser(Long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> delete(Long userId) {
        return delete("/" + userId);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> create(UserDto dto) {
        return post("", dto);
    }

    public ResponseEntity<Object> update(UserDto dto, Long userId) {
        return patch("/" + userId, dto);
    }
}