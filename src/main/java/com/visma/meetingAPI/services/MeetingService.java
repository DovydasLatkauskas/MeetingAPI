package com.visma.meetingAPI.services;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.MeetingCategory;
import com.visma.meetingAPI.models.MeetingType;
import com.visma.meetingAPI.models.Person;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingService {
    Meeting save(Meeting meeting);

    Meeting findMeetingById(String meetingId);

    boolean deleteMeeting(String meetingId);

    void updateMeeting(Meeting meeting);

    List<Meeting> getFilteredMeetings(String description, String responsiblePersonName, String category,
                                      String type, LocalDateTime startDate, LocalDateTime endDate,
                                      Integer minAttendees, Integer maxAttendees);

    boolean removeAttendee(String attendeeId);

    boolean isAttendeeInIntersectingMeeting(Person attendee, Meeting meeting);
}
