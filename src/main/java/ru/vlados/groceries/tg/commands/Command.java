package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

public interface Command {

    BotCommand getBotCommand();
    void execute(Update update, String[] command);

}
