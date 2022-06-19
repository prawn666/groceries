package ru.vlados.groceries.tg.commands.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;

import java.util.HashMap;
import java.util.List;

import com.pengrad.telegrambot.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.tg.client.TgClient;
import ru.vlados.groceries.tg.commands.Command;

@Slf4j
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
                //tg doesnt allow /notFound
                .map(Command::getBotCommand)
                .filter(botCommand -> !botCommand.command().equalsIgnoreCase("/notFound"))
                .toArray(BotCommand[]::new));
        tgClient.createConnection();
    }

    public Command getCommand(String commandName) {
        return commands.getOrDefault(commandName, commands.get("/notFound"));
    }

    private void sendSetMyCommands(BotCommand[] commands) {
        SetMyCommands setMyCommands = new SetMyCommands(commands);
        setMyCommands.languageCode("en");
        final BaseResponse responseFromSetMyCommands = tgClient.getBot().execute(setMyCommands);
        log.info("Got response from setMyCommands: {}", responseFromSetMyCommands);
    }

}
