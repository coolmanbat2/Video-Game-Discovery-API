package com.example.videogameapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class VideoGame {
    @NotEmpty(message = "Must have a title")
    private String title;
    @NotEmpty(message = "Must have a genre")
    private String genre;
    @NotEmpty(message = "Must have a platform")
    private String platform;
    @NotEmpty(message = "Must have a release date")
    private String release;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoGameId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setVideoGameId(Long videoGameId) {
        this.videoGameId = videoGameId;
    }

    public Long getVideoGameId() {
        return videoGameId;
    }
}
