package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CommandNotFound extends BasicCommand{

    @Autowired
    public CommandNotFound() {
        super("/notFound", "Command not found");
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        return Flux.just()
                .doOnNext(r -> sendMessage("такой команды нет", update.message().chat().id()));
    }
}
