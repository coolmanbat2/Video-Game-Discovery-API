package com.example.videogameapi.services;

import com.example.videogameapi.models.VideoGame;
import com.example.videogameapi.repositorires.VideoGameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;

@Service
public class VideoGameService {

    private final VideoGameRepo videoGameRepo;

    @Autowired
    public VideoGameService(VideoGameRepo videoGameRepo) {
        this.videoGameRepo = videoGameRepo;

    }


    public List<VideoGame> findGame(String title,
                                    String genre,
                                    String platform,
                                    String release) {
        List<VideoGame> result;
        if (title != null) {
            result = videoGameRepo.findByTitle(title);
        } else if (genre != null) {
            result = videoGameRepo.findByGenre(genre);
        } else if (platform != null) {
            result = videoGameRepo.findByPlatform(platform);
        } else if (release != null) {
            result = videoGameRepo.findByRelease(release);
        } else {
            result = videoGameRepo.findAll();
        }

        if (result.isEmpty()) {
            getLogger().error("Could not retrieve video games");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "There are no video games in the server.");
        } else {
            getLogger().info("Retrieved Video Games");
        }
        return result;
    }

    public void createVideoGame(@ModelAttribute VideoGame videoGame) {
        videoGameRepo.saveAndFlush(videoGame);
    }

    public boolean gameDoesNotExist(Long gameId) {
        return !videoGameRepo.existsById(gameId);
    }

    public void updateVideoGame(Long gameId, @ModelAttribute VideoGame videoGame) {
            videoGame.setVideoGameId(gameId);
            videoGameRepo.save(videoGame);
    }

    public void deleteVideoGame(@ModelAttribute VideoGame videoGame) {
        videoGameRepo.delete(videoGame);
    }



}
