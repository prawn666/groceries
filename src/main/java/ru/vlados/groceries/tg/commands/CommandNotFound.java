package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CommandNotFound extends BasicCommand {

    @Override
    public String getCommand() {
        return "/notFound";
    }

    @Override
    public String getDescription() {
        return "Command not found";
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        return Flux.just()
                .doOnNext(r -> sendMessage("такой команды нет", update.message().chat().id()));
    }
}
