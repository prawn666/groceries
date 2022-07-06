package ru.vlados.groceries.repository.dto;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("group_members")
public class GroupMember {

    @Id private long id;
    private Long userId;
    private Long groupId;
    private Instant createdAt;
}
