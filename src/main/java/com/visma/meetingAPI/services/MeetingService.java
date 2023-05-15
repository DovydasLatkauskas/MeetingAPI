package com.visma.meetingAPI.services;

import com.visma.meetingAPI.models.Meeting;

import java.util.UUID;

public interface MeetingService {
    public Meeting save(Meeting meeting);

    Meeting findMeetingById(String meetingId);

    void remove(String meetingId);

    void updateMeeting(Meeting meeting);
}
