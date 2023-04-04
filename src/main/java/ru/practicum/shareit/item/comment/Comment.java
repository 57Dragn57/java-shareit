package ru.practicum.shareit.item.comment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "comments")
@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String text;
    @Column(name = "item_id")
    long itemId;
    @Column(name = "author_id")
    long authorId;
    LocalDateTime created;
}
