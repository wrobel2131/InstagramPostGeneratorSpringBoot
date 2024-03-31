package com.InstaGenius.ResourceServer.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @PostAuthorize("hasAuthority('SCOPE_profile')")
    @GetMapping("/test1")
    public String testEndpoint1(@AuthenticationPrincipal Jwt jwt) {
        return "[testEndpoint1], scopes: profile: " + jwt.getSubject()+ " " + jwt.getClaims();
    }

    @PreAuthorize("hasAuthority('SCOPE_user.read')")
    @GetMapping("/test2")
    public String testEndpoint2(@AuthenticationPrincipal Jwt jwt) {
        return "[testEndpoint2], scopes: user.read: " + jwt.getSubject()+ " " + jwt.getClaims();
    }

    @PreAuthorize("hasAuthority('SCOPE_profile') and hasAnyAuthority('SCOPE_user.read')")
    @GetMapping("/test3")
    public String testEndpoint3(@AuthenticationPrincipal Jwt jwt) {
        return "[testEndpoint3], scopes: profile, user.read: " + jwt.getSubject() + " " + jwt.getClaims();
    }
}
