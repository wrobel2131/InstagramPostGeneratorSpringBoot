package com.InstagramPostGenerator.AuthorizationServer.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessFullAuthenticationResponse {
    String accessToken;
//    String refreshToken; //iw will be in response, when refreshing access token will be implemented
//    String userId; //It will be in response, when database will be implemented
    String username;
    Integer expiresIn;
    String tokenType;
}
