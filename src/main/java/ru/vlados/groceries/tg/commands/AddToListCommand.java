package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.db.GroceryItemsRepository;
import ru.vlados.groceries.repository.db.UserRepository;
import ru.vlados.groceries.repository.dto.GroceryItem;
import ru.vlados.groceries.tg.TgUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddToListCommand extends BasicCommand {

    private final GroceryItemsRepository groceryItemsRepository;
    private final UserRepository userRepository;

    @Override
    public String getCommand() {
        return "/add";
    }

    @Override
    public String getDescription() {
        return "Add staff to shopping list";
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        return userRepository.getUserByUserId(TgUtils.getUserId(update))
                .map(user -> createGrocery(command, user.getCurrentListId()))
                .flatMap(groceryItemsRepository::save)
                .map(row -> row)
                .flux()
                .doOnNext(row -> log.info(row.toString()));
    }

    private GroceryItem createGrocery(String[] command, long listId) {
        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setDone(false);
        groceryItem.setName(command[1]);
        groceryItem.setListId(listId);
        return groceryItem;
    }
}

