package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import lombok.Data;
import lombok.Getter;

@Getter
public class HelpCommand extends BasicCommand{

    public HelpCommand(String command, String description) {
        super(command, description);
    }

    @Override
    public void execute(Update update) {
        this.sendMessage("/help - помощь", update.message().chat().id());
    }
}
