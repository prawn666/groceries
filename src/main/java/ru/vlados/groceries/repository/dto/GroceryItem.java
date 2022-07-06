package ru.vlados.groceries.repository.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("grocery_items")
public class GroceryItem {

    @Id
    private Long id;
    private String name;
    private Boolean done;
    private Long listId;

}
