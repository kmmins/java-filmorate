CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment not null
    constraint USERS_PK
    primary key,
    EMAIL    VARCHAR(40) not null,
    LOGIN    VARCHAR(40) not null,
    NAME     VARCHAR(20),
    BIRTHDAY DATE
    );

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID INTEGER auto_increment not null
    constraint MPA_PK
    primary key,
    CODE    VARCHAR(5)
    );

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment not null
    constraint FILMS_PK
    primary key,
    NAME         VARCHAR(100) not null,
    DESCRIPTION  VARCHAR(200) not null,
    RELEASE_DATE DATE         not null,
    DURATION     INTEGER      not null,
    MPA_ID       INTEGER
    constraint FILMS_MPA_MPA_ID_FK
    references MPA
    );

CREATE TABLE IF NOT EXISTS FILMS_USERS_LIKES
(
    FILM_ID INTEGER not null
    constraint FILMS_USERS_LIKES_FILMS_FILM_ID_FK
    references FILMS,
    USER_ID INTEGER not null
    constraint FILMS_USERS_LIKES_USERS_USER_ID_FK
    references USERS,
    constraint FILMS_USERS_LIKES_PK
    primary key (FILM_ID, USER_ID)
    );

CREATE TABLE IF NOT EXISTS FRIENDS
(
    USER_THIS  INTEGER not null
    constraint FRIENDS_USERS_USER_ID_FK
    references USERS,
    USER_OTHER INTEGER not null
    constraint FRIENDS_USERS_USER_ID_FK_2
    references USERS,
    CONFIRMED  BOOLEAN,
    constraint FRIENDS_STATUS_STATUS_ID_FK
    primary key (USER_THIS, USER_OTHER)
    );

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER auto_increment not null
    constraint GENRE_PK
    primary key,
    NAME  VARCHAR(15) not null
    );

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null
    constraint FILM_GENRE_FILMS_FILM_ID_FK
    references FILMS,
    GENRE_ID INTEGER not null
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
    references GENRE,
    constraint FILM_GENRE_PK
    primary key (FILM_ID, GENRE_ID)
    );