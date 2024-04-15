package com.InstagramPostGenerator.AuthorizationServer.controller;
import com.InstagramPostGenerator.AuthorizationServer.model.LoginRequest;
import com.InstagramPostGenerator.AuthorizationServer.model.SuccessFullAuthenticationResponse;
import com.InstagramPostGenerator.AuthorizationServer.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<SuccessFullAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequest.getEmail(), loginRequest.getPassword()));
        return ResponseEntity.ok(tokenService.createSuccessFullAuthenticationResponse(authentication));
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<Boolean> isAuthenticated(@RequestHeader String Authorization){
        return ResponseEntity.ok(tokenService.validateAccessToken(Authorization));
    }


}
