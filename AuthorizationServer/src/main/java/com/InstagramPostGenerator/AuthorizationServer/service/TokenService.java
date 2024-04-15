package com.InstagramPostGenerator.AuthorizationServer.service;

import com.InstagramPostGenerator.AuthorizationServer.model.SuccessFullAuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder jwtDecoder;

    public SuccessFullAuthenticationResponse createSuccessFullAuthenticationResponse(Authentication authentication) {
        log.info("Authentication: " + authentication);
        String accessToken = generateAccessToken(authentication);
        String authenticatedUser = authentication.getName();
        return new SuccessFullAuthenticationResponse(accessToken, authenticatedUser, 3600, "Bearer");
    }


    public String generateAccessToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:9000")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Boolean validateAccessToken(String authorizationHeader) {
        //Simple checking, userId needs to be checked
        String accessToken = authorizationHeader.split(" ")[1];
        log.info("Access token: " +  accessToken);
        try {
            jwtDecoder.decode(accessToken);
            return true;

        } catch(JwtException exception) {
            log.info("Catch JwtException");
            log.info(exception.getMessage());
            return false;
        }
    }

}
