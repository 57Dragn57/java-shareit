package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment toComment(CommentDto comment) {
        Comment c = new Comment();
        c.setId(comment.getId());
        c.setText(comment.getText());

        return c;
    }

    public static CommentDto toCommentDto(Comment comment, UserStorage userStorage) {
        CommentDto c = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(userStorage.getUser(comment.getAuthorId()).getName())
                .created(comment.getCreated())
                .build();

        return c;
    }

    public static List<CommentDto> commentDtoList(List<Comment> comments, UserStorage userStorage, ItemStorage itemStorage) {
        List<CommentDto> cdto = new ArrayList<>();
        for (Comment c : comments) {
            cdto.add(toCommentDto(c, userStorage));
        }
        return cdto;
    }

}
