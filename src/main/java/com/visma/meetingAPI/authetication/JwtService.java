package com.visma.meetingAPI.authetication;

import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface JwtService {
    String extractUsername(String jwt);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
