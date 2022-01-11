package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class HelpCommand extends BasicCommand {

    public HelpCommand() {
        super("/help", "What I can do");
    }

    @Override
    public void execute(Update update, String[] command) {
        this.sendMessage("/help - помощь (лена красивая) лулка лулечка сладкое колечко",
            update.message().chat().id()); //todo убрать
    }
}
