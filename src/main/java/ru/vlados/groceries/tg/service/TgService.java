package ru.vlados.groceries.tg.service;

import com.pengrad.telegrambot.TelegramBot;
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
import ru.vlados.groceries.configuration.TgProps;
import ru.vlados.groceries.tg.client.TgClient;
import ru.vlados.groceries.tg.commands.BasicCommand;
import ru.vlados.groceries.tg.commands.HelpCommand;

@Service
@RequiredArgsConstructor
public class TgService {

    private final TgProps tgProps;
    private Map<String, BasicCommand> botCommandMap;

    @PostConstruct
    private void setClient() {
        botCommandMap = new HashMap<>();
        initCommands();
        sendSetMyCommands(botCommandMap.values()
            .stream()
            .map(BasicCommand::getBotCommand)
            .toArray(BotCommand[]::new));
    }

    public Flux<?> execute(Update update) {
        System.out.println(update);
        return Flux.just(update)
            .map(this::validate)
            .map(arr -> arr[0])
            .doOnNext(command -> botCommandMap.get(command).execute(update));
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
        addCommand(new HelpCommand("/help", "What I can do"));
    }

    private void sendSetMyCommands(BotCommand[] commands) {
        SetMyCommands setMyCommands = new SetMyCommands(commands);
        setMyCommands.languageCode(tgProps.getLanguageCode());
        bot.execute(setMyCommands);
    }

    private void addCommand(BasicCommand command) {
        botCommandMap.put(command.getBotCommand().command(), command);
    }

}
