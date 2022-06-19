package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import reactor.core.publisher.Flux;

public interface Command {

    BotCommand getBotCommand();
    Flux<?> execute(Update update, String[] command);

}
