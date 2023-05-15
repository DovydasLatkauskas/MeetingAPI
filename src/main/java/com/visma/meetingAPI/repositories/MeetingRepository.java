package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;

import java.util.List;

public interface MeetingRepository {
    List<Meeting> getMeetings();

    void save(Meeting meeting);

    Meeting findMeetingById(String meetingId);

    boolean deleteMeeting(String meetingId);

    boolean removeAttendee(String attendeeId, String meetingId);

    void addAttendee(Person attendee, String meetingId);
}
