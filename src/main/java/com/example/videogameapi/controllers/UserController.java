package com.example.videogameapi.controllers;

import com.example.videogameapi.datatransferobjects.AuthRequestDTO;
import com.example.videogameapi.datatransferobjects.JwtRequestDTO;
import com.example.videogameapi.models.VideoGameUser;
import com.example.videogameapi.services.JWTTokenService;
import com.example.videogameapi.services.VideoUserDetailsService;
import com.example.videogameapi.services.VideoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static com.example.videogameapi.configuration.DebugConfiguration.getLogger;

@RestController
public class UserController {

    private final String salt = BCrypt.gensalt();
    private final JWTTokenService jwtService;
    private final VideoUserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(JWTTokenService jwtService,
                          VideoUserService userService,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

//    @GetMapping("video-game-login")
//    public ResponseEntity<String> videoGameLogin(@RequestParam String username, @RequestParam String password) {
//        if (userService.checkLogin(username, password)) {
//            // Generate the JWT Token here.
//            String token = userService.generateToken(username);
//            getLogger().info(token);
//            return ResponseEntity.ok("user successfully logged in.");
//        } else {
//            getLogger().error("Could not login.");
//            return new ResponseEntity<>("Failed to login, or user doesn't exist.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    // TODO: Be able to add users into the database.
    @GetMapping("register")
    @ResponseBody
    public ResponseEntity<String> register(@ModelAttribute("VideoGameUser") VideoGameUser user) {

        try {
            // Check if user exists already.
            if (!userService.userExists(user.getUsername())) {
                // encrypt password
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userService.createUser(user);
            } else {
                getLogger().error("User has been registered already.");
            }

            getLogger().info("User has been registered.");
            return new ResponseEntity<>("User successfully registered.", HttpStatus.OK);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return new ResponseEntity<>("There was an error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("login")
    public ResponseEntity<JwtRequestDTO> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                JwtRequestDTO token = JwtRequestDTO.builder().accessToken(jwtService.generateToken(authRequestDTO.getUsername())).build();
                // The generated token would be set as a form of authentication.
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                throw new UsernameNotFoundException("Invalid user");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage()); // This part would be sent to the frontend.
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }


}
