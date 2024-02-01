package com.example.videogameapi.repositorires;

import com.example.videogameapi.models.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoGameRepo extends JpaRepository<VideoGame, Long> {
    List<VideoGame> findByTitle(String title);
    List<VideoGame> findByGenre(String genre);
    List<VideoGame> findByPlatform(String platform);
    List<VideoGame> findByRelease(String release);
}
