classDiagram
direction BT
class FILMS {
   character varying(100) NAME
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
   integer USER_THIS
   integer USER_OTHER
}
class GENRE {
   character varying(15) NAME
   integer GENRE_ID
}
class MPA {
   character varying(5) CODE
   integer MPA_ID
}
class USERS {
   character varying(40) EMAIL
   character varying(40) LOGIN
   character varying(20) NAME
   date BIRTHDAY
   integer USER_ID
}

FILMS  -->  MPA : MPA_ID
FILMS_USERS_LIKES  -->  FILMS : FILM_ID
FILMS_USERS_LIKES  -->  USERS : USER_ID
FILM_GENRE  -->  FILMS : FILM_ID
FILM_GENRE  -->  GENRE : GENRE_ID
FRIENDS  -->  USERS : USER_THIS:USER_ID
FRIENDS  -->  USERS : USER_OTHER:USER_ID
