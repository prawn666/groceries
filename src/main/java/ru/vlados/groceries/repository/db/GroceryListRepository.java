package ru.vlados.groceries.repository.db;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.dto.GroceryList;

public interface GroceryListRepository extends ReactiveCrudRepository<GroceryList, Long> {

    @Query("SELECT * FROM grocery_list JOIN grocery_units ON grocery_units.list_id = grocery_list.id WHERE grocery_units.list_id=$1")
    Flux<GroceryList> getListWithItems(long id);
}
