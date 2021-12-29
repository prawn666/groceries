package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

@Service
public class CommandNotFound extends BasicCommand{

    public CommandNotFound() {
        super("/notFound", "Команда не найдена");
    }

    @Override
    public void execute(Update update) {
        sendMessage("такой команды нет", update.message().chat().id());
    }
}
