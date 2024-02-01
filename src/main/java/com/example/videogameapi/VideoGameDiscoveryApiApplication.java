package com.example.videogameapi;

import com.example.videogameapi.controllers.VideoGameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideoGameDiscoveryApiApplication {
	public static void main(String[] args) {

		SpringApplication.run(VideoGameDiscoveryApiApplication.class, args);
	}

}
