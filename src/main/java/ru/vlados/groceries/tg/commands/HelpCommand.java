package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Getter
@Component
public class HelpCommand extends BasicCommand {

    public HelpCommand() {
        super("/help", "What I can do");
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        return Flux.just()
                .doOnNext(r -> this.sendMessage("/help - помощь (лена красивая) лулка лулечка сладкое колечко",
                        update.message().chat().id())); //todo убрать
    }
}
