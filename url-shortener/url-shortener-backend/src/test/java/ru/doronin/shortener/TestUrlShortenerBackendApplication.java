package ru.doronin.shortener;

import org.springframework.boot.SpringApplication;

public class TestUrlShortenerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(UrlShortenerBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
