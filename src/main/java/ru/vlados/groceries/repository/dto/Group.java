package ru.vlados.groceries.repository.dto;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("groups")
public class Group {

    @Id private long id;
    private long groupId;
    private Instant createdAt;
}
