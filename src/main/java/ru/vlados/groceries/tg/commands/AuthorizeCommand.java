package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.response.GetChatMemberResponse;

import java.io.IOException;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import ru.vlados.groceries.repository.dto.Group;
import ru.vlados.groceries.repository.dto.GroupMember;
import ru.vlados.groceries.repository.dto.User;

@Slf4j
@Component
public class AuthorizeCommand extends BasicCommand {

    private final R2dbcEntityTemplate template;

    @Autowired
    //todo при добавлении в группу регать ее
    public AuthorizeCommand(R2dbcEntityTemplate template) {
        super("/auth", "authorization");
        this.template = template;
    }

    @Override
    public Flux<?> execute(Update update, String[] command) {
        String chatId = command[1];
        Long userId = update.message().from().id();
        GetChatMember getChatMember = new GetChatMember(chatId, userId);

        return Flux.<GetChatMemberResponse>create(consumer -> tgClient.getBot()
                        .execute(getChatMember, createCallback(update, consumer)))
                .flatMap(response -> template.insert(createUserFromResponse(response, update))
                        .doOnError(this::onDuplicate)
                        .flatMap(user -> template.insert(createGroupFromUpdate(update)))
                        .flatMap(user -> template.insert(createGroupMemberFromResponse(response, update))))
                .doOnNext(
                        groupMember -> sendMessage("Вы зарегистрированы в группе",
                                update.message().chat().id()));
    }

    private Group createGroupFromUpdate(Update update) {
        Group group = new Group();
        group.setGroupId(update.message().chat().id());
        group.setCreatedAt(Instant.now());
        return group;
    }

    private void onDuplicate(Throwable throwable) {
        if (throwable instanceof DataIntegrityViolationException) {
            log.error("Duplicate");
        }
    }

    private Callback<GetChatMember, GetChatMemberResponse> createCallback(Update update,
                                                                          FluxSink<GetChatMemberResponse> consumer) {
        return new Callback<>() {
            @Override
            public void onResponse(GetChatMember request, GetChatMemberResponse response) {
                if (response.isOk()) {
                    consumer.next(response);
                } else {
                    consumer.error(new RuntimeException(
                            "Пользователь не найдет в группе " + update.message().chat().id()));
                }
            }

            @Override
            public void onFailure(GetChatMember request, IOException e) {
                consumer.error(new RuntimeException(
                        "Пользователь не найден в группе " + update.message().chat().id()));
            }
        };
    }

    private GroupMember createGroupMemberFromResponse(GetChatMemberResponse response,
                                                      Update update) {
        GroupMember groupMember = new GroupMember();
        groupMember.setUserId(response.chatMember().user().id());
        groupMember.setGroupId(update.message().chat().id());
        groupMember.setCreatedAt(Instant.now());
        return groupMember;
    }

    private User createUserFromResponse(GetChatMemberResponse response,
                                        Update update) {
        User user = new User();
        user.setUserId(response.chatMember().user().id());
        user.setCreatedAt(Instant.now());
        user.setActiveGroup(update.message().chat().id());
        return user;
    }

}
