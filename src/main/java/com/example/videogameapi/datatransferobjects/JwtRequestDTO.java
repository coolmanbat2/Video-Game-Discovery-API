package com.example.videogameapi.datatransferobjects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRequestDTO {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
