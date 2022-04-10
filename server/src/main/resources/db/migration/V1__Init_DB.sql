CREATE SCHEMA IF NOT EXISTS tinder;

create table tinder.usr
(
    user_id            int8 not null,
    description        varchar(255),
    name               varchar(2048),
    search_preferences int4,
    sex                int4,
    telegram_id        int8 not null,
    primary key (user_id)
);

create sequence hibernate_sequence start 1 increment 1;

create table tinder.user_user
(
    usr_id     int8 not null,
    favorit_id int8 not null
);
alter table if exists user_user
    add constraint FK4k3eax8ev0ugy3d8kgors13jw foreign key (favorit_id) references tinder.usr;
alter table if exists user_user
    add constraint FKb2u9rx4ab47aswdh8m4qdnu4q foreign key (usr_id) references tinder.usr;