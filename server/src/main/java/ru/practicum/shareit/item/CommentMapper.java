package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentDtoRequest comment) {
        Comment c = new Comment();
        c.setId(comment.getId());
        c.setText(comment.getText());

        return c;
    }

    public static CommentDtoResponse toCommentDto(Comment comment) {

        return CommentDtoResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDtoResponse> commentDtoList(List<Comment> comments) {
        List<CommentDtoResponse> cdto = new ArrayList<>();
        for (Comment c : comments) {
            cdto.add(toCommentDto(c));
        }
        return cdto;
    }
}
