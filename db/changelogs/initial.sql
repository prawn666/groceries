--liquibase formatted sql

--changeset prawn666:init

create TABLE groups
(
    id         BIGSERIAL PRIMARY KEY,
    group_id   BIGINT UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT UNIQUE,
    active_group BIGINT,
    current_list_id BIGINT REFERENCES grocery_list(id),
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create TABLE group_members
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (user_id),
    group_id   BIGINT REFERENCES groups (group_id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    UNIQUE (user_id, group_id)
);

-- todo один список общий а другой список временный (с добавлением в общий или нет)
create TABLE grocery_list
(
    id         BIGSERIAL PRIMARY KEY,
    group_id   BIGINT REFERENCES groups (group_id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

create TABLE grocery_items
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR,
    done            BOOL,
    list_id         BIGINT  REFERENCES grocery_list (id)
);