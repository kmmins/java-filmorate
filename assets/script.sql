create table "user"
(
    user_id  integer
        constraint user_pk
            primary key,
    email    varchar(40) not null,
    login    varchar(40) not null,
    name     varchar(20),
    birthday date
);

create table rating
(
    rating_id integer not null
        constraint rating_pk
            primary key,
    code      varchar(5)
);

create table film
(
    film_id      integer
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

create table status
(
    status_id integer not null
        constraint status_pk
            primary key,
    code      varchar(11)
);

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

create table genre
(
    genre_id integer     not null
        constraint genre_pk
            primary key,
    name     varchar(15) not null
);

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
