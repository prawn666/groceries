package ru.vlados.groceries.repository.db;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.vlados.groceries.repository.dto.GroceryList;

public interface GroceryListRepository extends ReactiveCrudRepository<GroceryList, Long> {

    @Query("UPDATE grocery_list "
        + "SET list = list || '{\"name\":\"qwer\",\"done\":false}'::jsonb WHERE group_id = $2")
    Mono<GroceryList> update(String s, Long id);

}
