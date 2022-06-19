package ru.vlados.groceries.repository.db;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.vlados.groceries.repository.dto.GroceryList;

public interface GroceryListRepository extends ReactiveCrudRepository<GroceryList, Long> {

    @Query("UPDATE grocery_list SET list = list || :stringJson::jsonb WHERE group_id = :id")
    Mono<GroceryList> update(Json stringJson, Long id);

}
