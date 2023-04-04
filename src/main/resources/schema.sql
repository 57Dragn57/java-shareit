create table if not exists users
(
    id    long generated always as identity primary key,
    name  varchar(30) not null,
    email varchar(30) not null
);
create unique index if not exists USER_EMAIL_UINDEX on USERS (email);

create table if not exists items
(
    id          long generated always as identity primary key,
    name        varchar(30)  not null,
    description varchar(200) not null,
    available   boolean,
    owner       long references users,
    request     long
);

create table if not exists bookings
(
    id         long generated always as identity primary key,
    start_date timestamp without time zone,
    end_date   timestamp without time zone,
    item_id    long references items,
    booker_id  long references users,
    status     varchar(35) NOT NULL
);

create table if not exists comments
(
    id        long generated always as identity primary key,
    text      varchar(200) NOT NULL,
    item_id   long references ITEMS,
    author_id long references USERS,
    created   timestamp without time zone
)