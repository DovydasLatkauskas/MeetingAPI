package com.visma.meetingAPI.authetication;
public interface JwtService {
    String extractUsername(String jwt);
}
