package ru.vlados.groceries.repository.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("grocery_items")
public class GroceryItem {

    @Id
    private long id;
    private String name;
    private boolean done;
    private long listId;

}
