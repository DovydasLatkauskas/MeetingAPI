package com.visma.meetingAPI.authetication.controllers;

import com.visma.meetingAPI.authetication.AuthenticationRequest;
import com.visma.meetingAPI.authetication.AuthenticationResponse;
import com.visma.meetingAPI.authetication.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationController {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request);

    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request);
}
