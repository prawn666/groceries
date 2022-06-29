package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.db.GroceryItemsRepository;
import ru.vlados.groceries.repository.dto.Grocery;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddToListCommand extends BasicCommand {

    private final GroceryItemsRepository template;

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
        return Flux.just(createGrocery(command, update.message().chat().id()))
                .flatMap(template::save)
                .map(row -> row)
                .doOnNext(row -> log.info(row.toString()));
    }

    private Grocery createGrocery(String[] command, long listId) {
        Grocery grocery = new Grocery();
        grocery.setDone(false);
        grocery.setName(command[1]);
        grocery.setListId(listId);
        return grocery;
    }
}

