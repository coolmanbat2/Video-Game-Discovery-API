package com.example.videogameapi.services;

import com.example.videogameapi.Exceptions.InvalidTokenException;
import com.example.videogameapi.models.VideoGameUser;
import com.example.videogameapi.repositorires.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;

@Service
public class VideoUserService {

    private final UserRepo userRepo;

    @Autowired
    public VideoUserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public void createUser(VideoGameUser user) {
       userRepo.saveAndFlush(user);
    }

    public boolean updateUser(Long userId, VideoGameUser user) {
        user.setVideoGameUserId(userId);
        userRepo.saveAndFlush(user);
        return true;
    }

    public boolean userExists(String username) {
        return !userRepo.findByUsername(username).isEmpty();
    }

    public boolean checkLogin(String username, String password) {
        if (userExists(username)) {
            VideoGameUser foundUser = userRepo.findByUsername(username).get(0);
            boolean userMatch = foundUser.getUsername().equals(username);
            boolean passMatch = BCrypt.checkpw(password, foundUser.getPassword());

            return userMatch && passMatch;
        } else {
            return false;
        }
    }

    public boolean deleteUser(VideoGameUser user) {
        userRepo.delete(user);
        return true;
    }





}
