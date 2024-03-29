DELETE FROM FILM_GENRE;
DELETE FROM FILM_GENRE;
DELETE FROM FEED;
DELETE FROM FILM_DIRECTOR;
DELETE FROM LIKES;
DELETE FROM FOLLOW;
DELETE FROM MOVIE;
DELETE FROM USERS;
DELETE FROM MPA;
DELETE FROM REVIEWS;
DELETE FROM DIRECTORS;

ALTER TABLE MOVIE ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE REVIEWS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FEED ALTER COLUMN EVENT_ID RESTART WITH 1;

MERGE INTO MPA (id, name)
        VALUES ( 1, 'G' ),
               ( 2, 'PG' ),
               ( 3, 'PG-13' ),
               ( 4, 'R' ),
               ( 5, 'NC-17' );

MERGE INTO GENRE (id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Фантастика'),
           (5, 'Триллер'),
           (6, 'Боевик');








