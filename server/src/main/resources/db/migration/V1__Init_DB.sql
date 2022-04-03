create sequence usr_id_seq start 1 increment 1;

create table user_user
(
    usr_id     int8 not null,
    favorit_id int8 not null
);

create table usr
(
    user_id            int8 not null,
    description        varchar(2048),
    name               varchar(255),
    search_preferences int4,
    sex                int4,
    telegram_id        int8 not null,
    primary key (user_id)
);

alter table if exists user_user add constraint FK4k3eax8ev0ugy3d8kgors13jw foreign key (favorit_id) references usr;
alter table if exists user_user add constraint FKb2u9rx4ab47aswdh8m4qdnu4q foreign key (usr_id) references usr;

CREATE  SEQUENCE IF NOT EXISTS usr_id_seq START WITH 1 INCREMENT BY 1;