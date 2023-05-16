package com.visma.meetingAPI.authetication.services;

import com.visma.meetingAPI.authetication.AuthenticationRequest;
import com.visma.meetingAPI.authetication.AuthenticationResponse;
import com.visma.meetingAPI.authetication.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
