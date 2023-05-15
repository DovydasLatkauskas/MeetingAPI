package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Meeting;

import java.util.List;

public interface MeetingRepository {
    List<Meeting> getMeetings();

    void save(Meeting meeting);

    Meeting findMeetingById(String meetingId);

    boolean deleteMeeting(String meetingId);

    void updateMeeting(Meeting meeting);

    boolean removeAttendee(String attendeeId, String meetingId);
}
