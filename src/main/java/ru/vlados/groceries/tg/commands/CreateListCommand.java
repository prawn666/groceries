package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.db.GroceryListRepository;
import ru.vlados.groceries.repository.db.UserRepository;
import ru.vlados.groceries.repository.dto.GroceryList;
import ru.vlados.groceries.tg.TgUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateListCommand extends BasicCommand {

    private final GroceryListRepository groceryListRepository;
    private final UserRepository userRepository;

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
                .flatMap(list -> userRepository.updateUserGroceryList(TgUtils.getUserId(update), list.getGroupId()))
                .flux()
                .doOnNext(stringObjectMap -> sendMessage("Successfully created list with name %s", update.chatMember().chat().id()));
    }
}
