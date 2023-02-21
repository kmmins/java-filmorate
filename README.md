# java-filmorate
Repository for another one homework project (filmorate).

filmorate database ER-diagram from [mermaid](https://github.com/kmmins/java-filmorate/blob/add-friends-likes/assets/ER_filmorate.md):
```mermaid
classDiagram
direction BT
class film {
   varchar(100) name
   varchar(200) description
   date release_date
   integer duration
   integer rating_id
   integer film_id
}
class film_genre {
   integer film_id
   integer genre_id
}
class films_users_likes {
   integer film_id
   integer user_id
}
class friends {
   integer status_id
   integer user_id1
   integer user_id2
}
class genre {
   varchar(15) name
   integer genre_id
}
class rating {
   varchar(5) code
   integer rating_id
}
class status {
   varchar(11) code
   integer status_id
}
class user {
   varchar(40) email
   varchar(40) login
   varchar(20) name
   date birthday
   integer user_id
}

film  -->  rating : rating_id
film_genre  -->  film : film_id
film_genre  -->  genre : genre_id
films_users_likes  -->  film : film_id
films_users_likes  -->  user : user_id
friends  -->  status : status_id
friends  -->  user : user_id2
friends  -->  user : user_id1
```
IDEA Ultimate auto-generated [schema](https://github.com/kmmins/java-filmorate/blob/add-friends-likes/assets/ER_filmorate.png) and [SQL script](https://github.com/kmmins/java-filmorate/blob/add-friends-likes/assets/script.sql).


