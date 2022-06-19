package ru.vlados.groceries.tg.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.repository.db.GroceryListRepository;
import ru.vlados.groceries.repository.dto.Grocery;

@Component
@Slf4j
public class AddToListCommand extends BasicCommand {

    public static final String UPDATE_QUERY = "UPDATE grocery_list SET list = list || '[%s]'::jsonb WHERE group_id = %d;";
    private final GroceryListRepository template;
    private final DatabaseClient template2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AddToListCommand(GroceryListRepository template,
                            DatabaseClient template2) {
        super("/add", "adds staff to list");
        this.template = template;
        this.template2 = template2;
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
