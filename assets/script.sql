create sequence users_user_id_seq
    as integer;

alter sequence users_user_id_seq owner to postgres;

create table "user"
(
    user_id  integer default nextval('users_user_id_seq'::regclass) not null
        constraint user_pk
            primary key,
    email    varchar(40)                                            not null,
    login    varchar(40)                                            not null,
    name     varchar(20),
    birthday date
);

alter table "user"
    owner to postgres;

alter sequence users_user_id_seq owned by "user".user_id;

create table rating
(
    rating_id integer not null
        constraint rating_pk
            primary key,
    code      varchar(5)
);

alter table rating
    owner to postgres;

create table film
(
    film_id      serial
        constraint film_pk
            primary key,
    name         varchar(100) not null,
    description  varchar(200) not null,
    release_date date         not null,
    duration     integer      not null,
    rating_id    integer
        constraint film_rating_rating_id_fk
            references rating
);

alter table film
    owner to postgres;

create table films_users_likes
(
    film_id integer not null
        constraint films_users_likes_film_film_id_fk
            references film,
    user_id integer not null
        constraint films_users_likes_user_user_id_fk
            references "user",
    constraint films_likes_pk
        primary key (film_id, user_id)
);

alter table films_users_likes
    owner to postgres;

create table status
(
    status_id integer not null
        constraint status_pk
            primary key,
    code      varchar(11)
);

alter table status
    owner to postgres;

create table friends
(
    user_this  integer not null
        constraint friends_user_user_id_fk
            references "user",
    user_other integer not null
        constraint friends_user_user_id_fk_2
            references "user",
    status_id  integer
        constraint friends_status_status_id_fk
            references status,
    constraint friends_pk
        primary key (user_this, user_other)
);

alter table friends
    owner to postgres;

create table genre
(
    genre_id integer     not null
        constraint genre_pk
            primary key,
    name     varchar(15) not null
);

alter table genre
    owner to postgres;

create table film_genre
(
    film_id  integer not null
        constraint film_genre_film_film_id_fk
            references film,
    genre_id integer not null
        constraint film_genre_genre_genre_id_fk
            references genre,
    constraint film_genre_pk
        primary key (film_id, genre_id)
);

alter table film_genre
    owner to postgres;


