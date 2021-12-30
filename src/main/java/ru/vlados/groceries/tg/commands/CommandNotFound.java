package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.tg.service.TgService;

@Service
public class CommandNotFound extends BasicCommand{

    @Autowired
    public CommandNotFound() {
        super("/notFound", "Команда не найдена");
    }

    @Override
    public void execute(Update update, String[] command) {
        sendMessage("такой команды нет", update.message().chat().id());
    }
}
