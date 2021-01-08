create table subject
(
    id        bigint  not null
        constraint subject_pkey
            primary key,
    content   varchar(255),
    headline  varchar(255),
    isdeleted boolean not null
);

alter table subject
    owner to postgres;

create table question
(
    id         bigint not null
        constraint question_pkey
            primary key,
    content    oid,
    owner      varchar(255),
    subject_id bigint not null
        constraint fk9gp9c9i6hp9ikei22cv1a3xpm
            references subject
);

alter table question
    owner to postgres;

