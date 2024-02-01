package com.example.videogameapi.configuration;

import com.example.videogameapi.controllers.VideoGameController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugConfiguration {
    private final static Logger logger = LoggerFactory.getLogger(VideoGameController.class);
    public static Logger getLogger() {
        return logger;
    }

}
