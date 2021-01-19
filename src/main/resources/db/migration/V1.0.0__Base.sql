
create table party
(
    id     bigint not null
        constraint party_pkey
            primary key,
    userid varchar(255)
);

alter table party
    owner to postgres;

create table subject
(
    id        bigint  not null
        constraint subject_pkey
            primary key,
    content   varchar(255),
    created   timestamp,
    headline  varchar(255),
    isdeleted boolean not null,
    ispublic  boolean not null
);

alter table subject
    owner to postgres;

create table question
(
    id         bigint  not null
        constraint question_pkey
            primary key,
    content    oid,
    ispublic   boolean not null,
    owner      varchar(255),
    subject_id bigint  not null
        constraint fk9gp9c9i6hp9ikei22cv1a3xpm
            references subject
);

alter table question
    owner to postgres;

create table comment
(
    id             bigint  not null
        constraint comment_pkey
            primary key,
    content        varchar(255),
    createdatetime timestamp,
    isdeleted      boolean not null,
    question_id    bigint  not null
        constraint fk1fn5pp8xah7v5pocq2f2kap62
            references question,
    user_id        bigint
        constraint fkq45222cw51pt6n9pho61h861m
            references party
);

alter table comment
    owner to postgres;

