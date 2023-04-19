package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory((serverUrl + "/items")))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsByUser(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> search(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> createComment(CommentDtoRequest dto, Long authorId, Long itemId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return post("/{itemId}/comment", authorId, parameters, dto);
    }

    public ResponseEntity<Object> addItem(ItemDtoRequest dto, Long userId) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> itemUpdate(ItemDtoRequest dto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, dto);
    }
}
