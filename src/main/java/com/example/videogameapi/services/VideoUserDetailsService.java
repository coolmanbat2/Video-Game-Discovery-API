package com.example.videogameapi.services;

import com.example.videogameapi.controllers.UserController;
import com.example.videogameapi.models.VideoGameUser;
import com.example.videogameapi.repositorires.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;

@Service
public class VideoUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public VideoUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    // Checks if the user exists in the repository. If it does not exist then the
    // login will fail. Otherwise, it will be passed onto the next stage of the
    // authentication process.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // check if user exists.
        VideoGameUser gameUser = userRepo.findByUsername(username).get(0);
        try {
            if (gameUser == null) {
                throw new UsernameNotFoundException("User doesn't exist!");
            } else {
                // if it exists, return user details object.
                return new VideoUserDetails(gameUser);
            }
        } catch (UsernameNotFoundException e) {
           getLogger().error(e.getMessage());
           return null;
        }
    }

}
