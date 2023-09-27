package com.project.userservice.services;

import com.project.userservice.entities.BlacklistToken;
import com.project.userservice.repositories.BlacklistTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlacklistTokenService {

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    public boolean isTokenBlacklisted(String token){
        Optional<BlacklistToken> blacklistToken = blacklistTokenRepository.findByToken(token);
        return blacklistToken.isPresent();
    }

    public void addTokenToBlacklist(HttpServletRequest request, HttpServletResponse response){
        final String authHeader = request.getHeader("Authorization");
        final String token;

        // Extract the JWT token from the Authorization header.
        token = authHeader.substring(7);

        // Clear the Authorization header in the response to log the user out.
        response.setHeader("Authorization", "");
        BlacklistToken blacklistToken = BlacklistToken.builder().token(token).build();
        blacklistTokenRepository.save(blacklistToken);
    }
}
