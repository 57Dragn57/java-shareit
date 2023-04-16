package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.dto.Request;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "requests")
    private Request requests;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;

        return hash;
    }
}
