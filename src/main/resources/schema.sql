
CREATE TABLE IF NOT EXISTS MPA (
    RATING_ID INT PRIMARY KEY AUTO_INCREMENT,
    RATING_MPA ENUM('G', 'PG', 'PG13', 'R', 'NC17') NOT NULL
);

CREATE TABLE IF NOT EXISTS GENRE (
    GENRE_ID INT PRIMARY KEY AUTO_INCREMENT,
    GENRE_CATEGORY ENUM('COMEDY','DRAMA','CARTOON','THRILLER','DOCUMENTARY','ACTION') NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM (
    FILM_ID INT PRIMARY KEY,
    FILM_NAME VARCHAR(255) NOT NULL,
    FILM_DESCRIPTION VARCHAR(255) NOT NULL,
    FILM_RELEASE_DATE DATE NOT NULL,
    FILM_DURATION INT NOT NULL,
    MPA_ID INT,
    GENRE_ID INT,
    constraint "FILM_MPA_ID" FOREIGN KEY(MPA_ID)
        REFERENCES MPA(RATING_ID),
    constraint "FILM_GENRE_ID" FOREIGN KEY(GENRE_ID)
        REFERENCES GENRE(GENRE_ID)
);

CREATE TABLE IF NOT EXISTS MOST_POPULAR_FILMS (
    FILM_ID INT NOT NULL,
    LIKE_COUNT INT NOT NULL,
    constraint "POPULAR_FILMS_FILM" FOREIGN KEY (FILM_ID)
        REFERENCES FILM(FILM_ID),
    constraint "POPULAR_FILMS_LIKE_COUNT" check (LIKE_COUNT > 0)
);

CREATE TABLE IF NOT EXISTS USERS (
    USER_ID INT PRIMARY KEY,
    USER_EMAIL VARCHAR(255) NOT NULL,
    USER_LOGIN VARCHAR(255) NOT NULL,
    USER_NAME VARCHAR(255) NOT NULL,
    USER_BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS_LIKE (
    FILM_ID INT NOT NULL,
    USER_ID INT NOT NULL,
    LIKE_FILM BOOLEAN NOT NULL,
    constraint "FILM_LIKE_FILM" FOREIGN KEY(FILM_ID)
        REFERENCES FILM(FILM_ID),
    constraint "FILM_LIKE_USER" FOREIGN KEY(USER_ID)
        REFERENCES USERS(USER_ID),
    constraint "FILM_LIKE_LIKE" check (LIKE_FILM = TRUE)
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
    STATUS_ID INT AUTO_INCREMENT,
    STATUS_USER_ID_1 INT NOT NULL,
    STATUS_USER_ID_2 INT NOT NULL,
    STATUS_REQUEST ENUM('WAITING_FOR_FRIENDSHIP_REQUEST','CONFIRMED','REJECTED') NOT NULL,
    FOREIGN KEY (STATUS_USER_ID_1) REFERENCES USERS(USER_ID),
    FOREIGN KEY (STATUS_USER_ID_2) REFERENCES USERS(USER_ID),
    UNIQUE(STATUS_USER_ID_1, STATUS_USER_ID_2)
);

CREATE TABLE IF NOT EXISTS MUTUAL_FRIENDS (
    USER_ID_1 INT NOT NULL,
    USER_ID_2 INT NOT NULL,
    MUTUAL_FRIENDS INT ARRAY,
    constraint "MUTUAL_FRIENDS_USER_1" FOREIGN KEY(USER_ID_1)
        REFERENCES USERS(USER_ID),
    constraint "MUTUAL_FRIENDS_USER_2" FOREIGN KEY(USER_ID_2)
        REFERENCES USERS(USER_ID),
    UNIQUE(USER_ID_1, USER_ID_2)
);