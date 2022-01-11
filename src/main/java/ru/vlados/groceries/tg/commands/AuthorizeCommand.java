package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeCommand extends BasicCommand {

    @Autowired
    public AuthorizeCommand() {
        super("/auth", "authorization");
    }

    @Override
    public void execute(Update update, String[] command) {
        String chatId = command[1];
        Long userId = update.message().from().id();
        GetChatMember getChatMember = new GetChatMember(chatId, userId);

        tgClient.getBot().execute(getChatMember,
            new Callback<GetChatMember, GetChatMemberResponse>() {
                @Override
                public void onResponse(GetChatMember request, GetChatMemberResponse response) {
                    if (response.isOk()) {
                        sendMessage("Вы зарегистрированы в группе", update.message().chat().id());
                    } else {
                        onFailure(request, null);
                    }
                }

                @Override
                public void onFailure(GetChatMember request, IOException e) {
                    sendMessage("Пользователь не найдет в группе ", update.message().chat().id());
                }
            });

    }

    @Override
    public BotCommand getBotCommand() {
        return botCommand;
    }

}
