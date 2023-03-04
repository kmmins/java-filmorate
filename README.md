# java-filmorate
Repository for homework project filmorate.

filmorate database ER-diagram [mermaid](https://github.com/kmmins/java-filmorate/blob/add-database/assets/erd_filmorate.md):
```mermaid
classDiagram
direction BT
class FILMS {
   character varying(100) FILM_NAME
   character varying(200) DESCRIPTION
   date RELEASE_DATE
   integer DURATION
   integer MPA_ID
   integer FILM_ID
}
class FILMS_USERS_LIKES {
   integer FILM_ID
   integer USER_ID
}
class FILM_GENRE {
   integer FILM_ID
   integer GENRE_ID
}
class FRIENDS {
   boolean CONFIRMED
   integer USER_ID_THIS
   integer USER_ID_OTHER
}
class GENRE {
   character varying(15) GENRE_NAME
   integer GENRE_ID
}
class MPA {
   character varying(5) CODE
   integer MPA_ID
}
class USERS {
   character varying(40) EMAIL
   character varying(40) LOGIN
   character varying(20) USER_NAME
   date BIRTHDAY
   integer USER_ID
}

FILMS  -->  MPA : MPA_ID
FILMS_USERS_LIKES  -->  FILMS : FILM_ID
FILMS_USERS_LIKES  -->  USERS : USER_ID
FILM_GENRE  -->  FILMS : FILM_ID
FILM_GENRE  -->  GENRE : GENRE_ID
FRIENDS  -->  USERS : USER_ID_THIS
FRIENDS  -->  USERS : USER_ID_OTHER
```
Also see IDEA Ultimate auto-generated ER-diagram [erd_filmorate.png](https://github.com/kmmins/java-filmorate/blob/add-database/assets/erd_filmorate.png).

SQL-script here: [schema.sql](https://github.com/kmmins/java-filmorate/blob/add-database/src/main/resources/schema.sql).
