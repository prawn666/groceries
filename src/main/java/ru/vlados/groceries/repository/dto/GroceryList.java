package ru.vlados.groceries.repository.dto;

import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
public class GroceryList {

    @Id private long id;
    private long groupId;
    private Instant createdAt;
}
