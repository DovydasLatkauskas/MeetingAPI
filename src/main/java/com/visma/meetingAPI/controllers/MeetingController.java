package com.visma.meetingAPI.controllers;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingController {
    ResponseEntity<String> createMeeting(Meeting meeting);

    ResponseEntity<String> deleteMeeting(String meetingId);

    ResponseEntity<String> addAttendee(String meetingId, Person attendee);
    ResponseEntity<String> removeAttendee(String meetingId, Person attendee);
    ResponseEntity<List<Meeting>> getMeetings(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String responsiblePerson,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer minAttendees,
            @RequestParam(required = false) Integer maxAttendees
    );
}
