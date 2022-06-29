package ru.vlados.groceries.repository.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.vlados.groceries.repository.dto.GroceryItem;

public interface GroceryItemsRepository extends ReactiveCrudRepository<GroceryItem, Long> {
}
