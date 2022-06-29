package ru.vlados.groceries.repository.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Grocery {

    @Id
    private long id;
    private String name;
    private boolean done;
    private long listId;

}
