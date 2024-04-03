package com.InstaGenius.AuthorizationServer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {

    //w configu dodalem do permit all, ale cos trzeba xmienic, bo on nie sprawdza sesji (z ciasteczka), tylko uwierzytelnienie tutaj2
    @GetMapping("/auth/status")
    public Boolean isAuthenticated(Authentication authentication) {
        log.info(String.valueOf(authentication.isAuthenticated()));
        return authentication.isAuthenticated();
    }
}
