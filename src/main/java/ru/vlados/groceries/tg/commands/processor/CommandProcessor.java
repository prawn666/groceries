package ru.vlados.groceries.tg.commands.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.tg.client.TgClient;
import ru.vlados.groceries.tg.commands.Command;

@Service
public class CommandProcessor {

    private final HashMap<String, Command> commands;
    private final TgClient tgClient;

    @Autowired
    public CommandProcessor(List<Command> commandList, TgClient tgClient) {
        this.commands = new HashMap<>(commandList.size());
        this.tgClient = tgClient;

        commandList.forEach(command -> commands.put(command.getBotCommand().command(), command));

        sendSetMyCommands(commands.values()
            .stream()
            .map(Command::getBotCommand)
            .toArray(BotCommand[]::new));
        tgClient.createConnection();
    }

    public Command getCommand(String commandName) {
        return commands.getOrDefault(commandName, commands.get("/notFound"));
    }

    private void sendSetMyCommands(BotCommand[] commands) {
        SetMyCommands setMyCommands = new SetMyCommands(commands);
        tgClient.getBot().execute(setMyCommands);
    }

}
