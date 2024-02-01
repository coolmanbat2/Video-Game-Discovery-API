package com.example.videogameapi.repositorires;

import com.example.videogameapi.models.VideoGameUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<VideoGameUser, Long> {
  List<VideoGameUser> findByUsername(String username);

  List<VideoGameUser> findByPassword(String password);
}
