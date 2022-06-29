package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.db.GroceryListRepository;
import ru.vlados.groceries.repository.dto.GroceryList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateListCommand extends BasicCommand {

    private final DatabaseClient template2;
    private final GroceryListRepository groceryListRepository;

    @Override
    public String getCommand() {
        return "/createList";
    }

    @Override
    public String getDescription() {
        return "Create new shopping list";
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        final GroceryList entity = new GroceryList();
        entity.setGroupId(update.message().chat().id());

        return groceryListRepository.save(entity)
                .flux()
                .doOnNext(stringObjectMap -> sendMessage("Successfully created list with name %s", update.chatMember().chat().id()));
    }
}
