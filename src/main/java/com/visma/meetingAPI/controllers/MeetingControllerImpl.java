package com.visma.meetingAPI.controllers;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.repositories.PersonRepository;
import com.visma.meetingAPI.services.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MeetingControllerImpl implements MeetingController {
    private final MeetingService meetingService;
    private final PersonRepository personRepository;
    public MeetingControllerImpl(MeetingService meetingService, PersonRepository personRepository) {
        this.meetingService = meetingService;
        this.personRepository = personRepository;
    }

    @GetMapping("/")
    public String helloWorld(){
        return "Hello world!";
    } // TODO delete

    @Override
    @PostMapping("/create-meeting")
    public ResponseEntity<String> createMeeting(@RequestBody Meeting meeting){
        meetingService.save(meeting); // .save returns the saved meeting with a generated id
        return ResponseEntity.status(HttpStatus.CREATED).body(meeting.getId());
    }

    @Override
    @DeleteMapping("/delete-meeting/{meetingId}")
    public ResponseEntity<String> deleteMeeting(@PathVariable String meetingId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);
        // TODO create authentication and authorization
        // Principal principal = authentication.getPrincipal();

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }
// TODO create authentication and authorization

//        if (!isAuthorized(meeting, principal)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        if (meetingService.deleteMeeting(meetingId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting not found.");
        }
    }
    // TODO create authentication and authorization

//    private boolean isAuthorized(Meeting meeting, Principal principal) {
//        return meeting.getResponsiblePerson().getId().equals(principal.getName());
//    }

    @Override
    @PostMapping("/{meetingId}/add-attendee/{attendeeId}")
    public ResponseEntity<String> addAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);
        if(meeting == null){
            return ResponseEntity.notFound().build();
        }
        Person attendee = personRepository.findPersonById(attendeeId);
        if(attendee == null){
            return ResponseEntity.notFound().build();
        }

        if (meeting.getAttendees().contains(attendee)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attendee is already added to the meeting.");
        }

        if (meetingService.isAttendeeInIntersectingMeeting(attendee, meeting)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attendee is already in a conflicting meeting.");
        }

        meetingService.addAttendee(attendee, meetingId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @DeleteMapping("/{meetingId}/remove-attendee/{attendeeId}")
    public ResponseEntity<String> removeAttendee(@PathVariable String meetingId, @PathVariable String attendeeId) {
        Meeting meeting = meetingService.findMeetingById(meetingId);

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }
        if(personRepository.findPersonById(attendeeId) == null){
            return ResponseEntity.notFound().build();
        }

        Person responsiblePerson = meeting.getResponsiblePerson();

        if (responsiblePerson.getId().equals(attendeeId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The responsible person cannot be removed from the meeting.");
        }

        if (meetingService.removeAttendee(attendeeId, meetingId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendee not found in the meeting.");
        }
    }

    // we get all meetings and filter our those meeting that do not match the (optional) filters
    @Override
    @GetMapping("/get-meetings")
    public ResponseEntity<List<Meeting>> getMeetings(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String responsiblePersonName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer minAttendees,
            @RequestParam(required = false) Integer maxAttendees
    ) {
        List<Meeting> filteredMeetings = meetingService.getFilteredMeetings(
                description, responsiblePersonName, category, type, startDate, endDate, minAttendees, maxAttendees);

        return ResponseEntity.ok(filteredMeetings);
    }
}
