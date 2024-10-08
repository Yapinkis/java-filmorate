INSERT INTO GENRE (GENRE_ID, GENRE_NAME) VALUES (1, 'Комедия');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME) VALUES (2, 'Драма');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME) VALUES (3, 'Мультфильм');
INSERT INTO GENRE (GENRE_ID, GENRE_NAME) VALUES (4, 'Триллер');

INSERT INTO USERS(USER_EMAIL,USER_LOGIN,USER_NAME,USER_BIRTHDAY)
VALUES('something@ya.ru','login_One','Test_Name_One','1985-11-10');

INSERT INTO USERS(USER_EMAIL,USER_LOGIN,USER_NAME,USER_BIRTHDAY)
VALUES('other@ya.ru','login_Two','Test_Name_Two','1990-08-12');

INSERT INTO USERS(USER_EMAIL,USER_LOGIN,USER_NAME,USER_BIRTHDAY)
VALUES('maybe@ya.ru','login_Three','Test_Name_Three','1995-04-06');

INSERT INTO USERS(USER_EMAIL,USER_LOGIN,USER_NAME,USER_BIRTHDAY)
VALUES('new@ya.ru','login_Four','Test_Name_Four','1993-02-03');

INSERT INTO MPA (RATING_ID, RATING_NAME) VALUES (1, 'G');
INSERT INTO FILM(FILM_NAME,DESCRIPTION,DURATION,RELEASE_DATE,RATING_ID)
VALUES('One_Film', 'One_Description', 110, '1999-12-11', 1);

INSERT INTO MPA (RATING_ID, RATING_NAME) VALUES (2, 'PG');
INSERT INTO FILM(FILM_NAME,DESCRIPTION,DURATION,RELEASE_DATE,RATING_ID)
VALUES('Two_Film', 'Two_Description', 120, '1995-10-04', 2);

INSERT INTO MPA (RATING_ID, RATING_NAME) VALUES (3, 'PG-13');
INSERT INTO FILM(FILM_NAME,DESCRIPTION,DURATION,RELEASE_DATE,RATING_ID)
VALUES('Three_Film', 'Three_Description', 130, '2024-01-22', 3);

INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (1, 1);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (2, 2);
INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (3, 3);