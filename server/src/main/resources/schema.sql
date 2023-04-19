create table if not exists users
(
    id    bigint generated always as identity primary key,
    name  varchar(30) not null,
    email varchar(30) not null
);
create unique index if not exists USER_EMAIL_UINDEX on USERS (email);

create table if not exists requests
(
    id          bigint generated always as identity primary key,
    description varchar(300),
    requestor   bigint references USERS,
    created     timestamp without time zone
);

create table if not exists items
(
    id          bigint generated always as identity primary key,
    name        varchar(30)  not null,
    description varchar(200) not null,
    available   boolean,
    owner       bigint references users,
    requests    bigint references requests
);

create table if not exists bookings
(
    id         bigint generated always as identity primary key,
    start_date timestamp without time zone,
    end_date   timestamp without time zone,
    item_id    bigint references items,
    booker_id  bigint references users,
    status     varchar(35) NOT NULL
);

create table if not exists comments
(
    id        bigint generated always as identity primary key,
    text      varchar(200) NOT NULL,
    item_id   bigint references ITEMS,
    author_id bigint references USERS,
    created   timestamp without time zone
);

