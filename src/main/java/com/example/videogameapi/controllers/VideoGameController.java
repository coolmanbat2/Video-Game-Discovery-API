package com.example.videogameapi.controllers;

import com.example.videogameapi.Exceptions.VideoGameNotFoundException;
import com.example.videogameapi.models.VideoGame;
import com.example.videogameapi.services.VideoGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;

@RestController
public class VideoGameController {

    private final VideoGameService videoGameService;

    public VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @GetMapping("get")
    @ResponseBody
    public List<VideoGame> get(@RequestParam(required = false) String title,
                               @RequestParam(required = false) String genre,
                               @RequestParam(required = false) String platform,
                               @RequestParam(required = false) String release) {

        return videoGameService.findGame(title, genre, platform, release);
    }
    @PostMapping("add")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@ModelAttribute VideoGame videoGame) {
        getLogger().info("Inside Add function.");
        try {
            videoGameService.createVideoGame(videoGame);
            getLogger().info("Saved Video Game data to server.");
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The information you gave is not enough to save to the server.");
        }
    }

    @GetMapping("update")
    public void update(Long gameId, @ModelAttribute VideoGame videoGame) {
        try {

            if (videoGameService.gameDoesNotExist(gameId)) {
                throw new VideoGameNotFoundException("The video game you tried doesn't exist.");
            } else {
                videoGameService.updateVideoGame(gameId, videoGame);
                getLogger().info("Updated Video Game");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not locate specific Video Game and update it.");
        }

    }
    @GetMapping("delete")
    public void delete(@ModelAttribute VideoGame videoGame) {
        try {
            if (videoGameService.gameDoesNotExist(videoGame.getVideoGameId())) {
                throw new VideoGameNotFoundException("The video game you tried doesn't exist.");
            } else {
                videoGameService.deleteVideoGame(videoGame);
                getLogger().info("Deleted Video Game");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Could not locate specific Video Game and delete it.");
        }
    }

}
