package ru.vlados.groceries.tg.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.db.GroceryListRepository;
import ru.vlados.groceries.repository.dto.Grocery;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddToListCommand extends BasicCommand {

    private final GroceryListRepository template;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        return Flux.just(createGrocery(command))
                .map(this::mapToJson)
                .flatMap(jsonGrocery -> template.update(Json.of(jsonGrocery), update.message().chat().id()))
                .map(row -> row)
                .doOnNext(row -> log.info(row.toString()));
    }

    private String mapToJson(Grocery grocery) {
        try {
            return objectMapper.writeValueAsString(grocery);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Grocery createGrocery(String[] command) {
        Grocery grocery = new Grocery();
        grocery.setDone(false);
        grocery.setName(command[1]);
        return grocery;
    }
}

