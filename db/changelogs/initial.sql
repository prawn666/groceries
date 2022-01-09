--liquibase formatted sql

--changeset prawn666:init

CREATE TABLE groups
(
    id       BIGSERIAL PRIMARY KEY,
    group_id BIGINT UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE users
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE group_members
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT REFERENCES users (user_id),
    group_id BIGINT REFERENCES groups (group_id),
    created_at TIMESTAMP WITH TIME ZONE,
    UNIQUE (user_id, group_id)
);
