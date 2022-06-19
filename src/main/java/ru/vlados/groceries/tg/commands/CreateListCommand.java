package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class CreateListCommand extends BasicCommand {

    private final DatabaseClient template2;

    public CreateListCommand(DatabaseClient template2) {
        super("/createList", "Create new shopping list");
        this.template2 = template2;
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        return template2.sql(String.format("INSERT INTO grocery_list(group_id) VALUES (%d) RETURNING", update.message().chat().id()))
                .fetch()

                .first()
                .flux()
                .doOnNext(stringObjectMap -> sendMessage("Successfully created list with name %s", update.chatMember().chat().id()));
    }
}
