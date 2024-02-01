package com.example.videogameapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Entity
public class VideoGameUser {

    @NotEmpty(message = "Must fill out a proper username. ")
    private String username;
    @NotEmpty(message = "Password is required for security.")
    private String password;
    @NotEmpty(message = "e-mail is required for account registration")
    private String email;

    @Id
    @GeneratedValue
    private Long videoGameUserId;

    public Set<VideoGameUserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<VideoGameUserRole> roles) {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<VideoGameUserRole> roles = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public Long getVideoGameUserId() {
        return videoGameUserId;
    }

    public void setVideoGameUserId(Long videoGameUserId) {
        this.videoGameUserId = videoGameUserId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
