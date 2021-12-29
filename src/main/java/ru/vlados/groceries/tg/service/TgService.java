package ru.vlados.groceries.tg.service;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.tg.client.TgClient;
import ru.vlados.groceries.tg.commands.BasicCommand;
import ru.vlados.groceries.tg.commands.CommandNotFound;
import ru.vlados.groceries.tg.commands.HelpCommand;

@Service
@RequiredArgsConstructor
public class TgService {

    private final TgClient tgClient;
    private final HelpCommand helpCommand;
    private final CommandNotFound commandNotFound;
    private Map<String, BasicCommand> botCommandMap;

    @PostConstruct
    private void setClient() {
        botCommandMap = new HashMap<>();
        initCommands();
        sendSetMyCommands(botCommandMap.values()
            .stream()
            .map(BasicCommand::getBotCommand)
            .toArray(BotCommand[]::new));
        tgClient.createConnection();
    }

    public Flux<?> execute(Update update) {
        return Flux.just(update)
            .map(this::validate)
            .map(arr -> arr[0])
            .doOnNext(command -> botCommandMap.getOrDefault(command, commandNotFound).execute(update));
    }

    private String[] validate(Update update) {
        if (!StringUtils.hasText(update.message().text())) {
            //todo норм эксепшен
            throw new RuntimeException("empty message");
        }
        String[] splitText = update.message().text().split(" ");
        if (splitText.length == 0) {
            throw new RuntimeException("empty message");
        }
        return splitText;
    }

    private void initCommands() {
        addCommand(this.helpCommand);
    }

    private void sendSetMyCommands(BotCommand[] commands) {
        SetMyCommands setMyCommands = new SetMyCommands(commands);
//        setMyCommands.languageCode(tgProps.getLanguageCode());
        tgClient.getBot().execute(setMyCommands);
    }

    private void addCommand(BasicCommand command) {
        botCommandMap.put(command.getBotCommand().command(), command);
    }

}
