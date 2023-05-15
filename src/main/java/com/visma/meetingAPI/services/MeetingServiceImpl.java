package com.visma.meetingAPI.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeetingServiceImpl implements MeetingService{
    private String generateMeetingId() {
        // UUIDs are extremely unlikely to collide, so we do not check for uniqueness
        // however this check can be added at the expense of performance
        return UUID.randomUUID().toString();
    }
}
