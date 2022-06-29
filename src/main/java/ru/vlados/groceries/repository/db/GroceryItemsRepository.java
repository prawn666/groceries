package ru.vlados.groceries.repository.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.vlados.groceries.repository.dto.Grocery;
import ru.vlados.groceries.repository.dto.GroceryList;

public interface GroceryItemsRepository extends ReactiveCrudRepository<Grocery, Long> {
}
