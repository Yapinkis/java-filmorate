package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {
	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
	//Я не могу понять, почему у меня в Postman на одной машине проходятся все тесты, а при поптыке пройти их на
	// другом ПК иногда возникают ошибки по типу
	// Status code is 200 | AssertionError: expected response to have status reason 'OK' but got 'BAD REQUEST

}
