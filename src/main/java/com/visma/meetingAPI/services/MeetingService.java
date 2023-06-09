package com.visma.meetingAPI.services;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingService {
    Meeting save(Meeting meeting);

    Meeting findMeetingById(String meetingId);

    boolean deleteMeeting(String meetingId);

    List<Meeting> getFilteredMeetings(String description, String responsiblePersonName, String category,
                                      String type, LocalDateTime startDate, LocalDateTime endDate,
                                      Integer minAttendees, Integer maxAttendees);

    boolean removeAttendee(String attendeeId, String meetingId);

    boolean isAttendeeInIntersectingMeeting(Person attendee, Meeting meeting);

    void addAttendee(Person attendee, String meetingId);
}
