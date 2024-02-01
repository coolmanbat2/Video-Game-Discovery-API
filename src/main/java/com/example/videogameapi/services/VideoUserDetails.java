package com.example.videogameapi.services;

import com.example.videogameapi.models.VideoGameUser;
import com.example.videogameapi.models.VideoGameUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO:
public class VideoUserDetails implements UserDetails {

    private String username;
    private String password;
    private String email;
    private final Collection<? extends GrantedAuthority> authorities;


    // Adds appropriate User information after a user logs in successfully.
    public VideoUserDetails(VideoGameUser gameUser) {
        this.username = gameUser.getUsername();
        this.password = gameUser.getPassword();
        this.email = gameUser.getEmail();

        List<GrantedAuthority> auths = new ArrayList<>();
        for (VideoGameUserRole role : gameUser.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getRoleName().toUpperCase()));
        }
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
       return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
