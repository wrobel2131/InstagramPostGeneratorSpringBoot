package com.InstagramPostGenerator.ResourceServer.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource")
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String testEndpoint() {

        log.info("ENDPOINT LOG");
        return "Test endpoint works";
    }
}
