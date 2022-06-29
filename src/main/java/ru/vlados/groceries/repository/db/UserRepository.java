package ru.vlados.groceries.repository.db;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.vlados.groceries.repository.dto.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> getUserByUserId(long userId);
    @Query("UPDATE users SET current_list_id = $2 WHERE user_id = $1 RETURNING *;")
    Mono<User> updateUserGroceryList(long userId, long groceryListId);
}
