package com.visma.meetingAPI.controllers;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.services.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MeetingControllerImpl implements MeetingController {
    private final MeetingService meetingService;
    public MeetingControllerImpl(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/")
    public String hello(){
        return "Hello world!";
    } // TODO delete

    @Override
    @PostMapping("/create-meeting")
    public ResponseEntity<String> createMeeting(@RequestBody Meeting meeting){
        meeting = meetingService.save(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).body(meeting.toString());
    }

    @Override
    @DeleteMapping("/delete-meeting")
    public ResponseEntity<String> deleteMeeting(@RequestBody String meetingId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isAuthorized(meeting)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (meetingService.deleteMeeting(meetingId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting not found.");
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{meetingId}/add-attendee/{attendeeId}")
    public ResponseEntity<String> addAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        if (meeting.getAttendees().contains(attendee)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attendee is already added to the meeting.");
        }

        if (meetingService.isAttendeeInIntersectingMeeting(attendee, meeting)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attendee is already in a conflicting meeting.");
        }

        meeting.addAttendee(attendee);
        meetingService.updateMeeting(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @DeleteMapping("/{meetingId}/remove-attendee/{attendeeId}")
    public ResponseEntity<String> removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        Person responsiblePerson = meeting.getResponsiblePerson();

        if (responsiblePerson.getId().equals(attendeeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The responsible person cannot be removed from the meeting.");
        }

        if (meetingService.removeAttendee(attendeeId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendee not found in the meeting.");
        }
    }

    // we get all meetings and filter our those meeting that do not match the (optional) filters
    @Override
    public ResponseEntity<List<Meeting>> getMeetings(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String responsiblePerson,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer minAttendees,
            @RequestParam(required = false) Integer maxAttendees
    ) {
        List<Meeting> filteredMeetings = meetingService.getFilteredMeetings(
                description, responsiblePerson, category, type, startDate, endDate, minAttendees, maxAttendees);

        return ResponseEntity.ok(filteredMeetings);
    }
}
