package ru.vlados.groceries.repository.dto;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class User {

    @Id private long id;
    private long userId;
    private long activeGroup;
    private long currentListId;
    private Instant createdAt;
}
