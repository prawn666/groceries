package ru.vlados.groceries.tg.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import ru.vlados.groceries.repository.db.GroceryListRepository;
import ru.vlados.groceries.repository.dto.Grocery;

@Component
@Slf4j
public class AddToListCommand extends BasicCommand {

    private final GroceryListRepository template;
    private final DatabaseClient template2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AddToListCommand(GroceryListRepository template,
        DatabaseClient template2) {
        super("/add", "adds staff to list");
        this.template = template;
        this.template2 = template2;
    }

    @Override
    public void execute(Update update, String[] command) {

        Grocery grocery = new Grocery();
        grocery.setDone(false);
        grocery.setName(command[1]);

        String query = null;
        try {
            template.update(objectMapper.writeValueAsString(grocery), update.message().chat().id())
                .subscribe(res -> sendMessage(res.toString(), update.message().chat().id()));
            query = "UPDATE grocery_list "
                + "SET list = list || '" + objectMapper.writeValueAsString(grocery) + "'::jsonb "
                + "WHERE group_id = " + update.message().chat().id() + ";";
            template2.sql(query)
                .map(row -> row)
                .all()
                .subscribe();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.info("lol");
        }

    }
}
