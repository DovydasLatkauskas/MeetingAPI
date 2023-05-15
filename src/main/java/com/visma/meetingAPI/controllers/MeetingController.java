package com.visma.meetingAPI.controllers;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.services.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeetingController {
     private final MeetingService meetingService;
     public MeetingController(MeetingService meetingService) {
            this.meetingService = meetingService;
     }

    @GetMapping("/")
    public String hello(){
        return "Hello world!";
    }

    @PostMapping("/create-meeting")
    public ResponseEntity<String> createMeeting(@RequestBody Meeting meeting){
        meeting = meetingService.save(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).body(meeting.toString());
    }

    @DeleteMapping("/delete-meeting")
    public ResponseEntity<String> deleteMeeting(@RequestBody String meetingId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isAuthorized(meeting)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        meetingService.remove(meetingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{meetingId}/attendees")
    public ResponseEntity<String> addAttendee(@PathVariable String meetingId, @RequestBody Person attendee) {
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
}
