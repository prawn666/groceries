package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;

public interface Command {

    void execute(Update update);

}
