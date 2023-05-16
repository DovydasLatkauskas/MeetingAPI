package com.visma.meetingAPI.controllers;

import com.visma.meetingAPI.authetication.services.JwtService;
import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.repositories.PersonRepository;
import com.visma.meetingAPI.services.MeetingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MeetingControllerImpl implements MeetingController {
    private final MeetingService meetingService;
    private final PersonRepository personRepository;
    private final JwtService jwtService;

    /**
     * Constructs a new MeetingControllerImpl with the given services and repositories.
     *
     * @param meetingService   the meeting service to handle meeting-related operations
     * @param personRepository the person repository to access person data
     * @param jwtService       the JWT service for authentication and authorization
     */
    public MeetingControllerImpl(MeetingService meetingService, PersonRepository personRepository, JwtService jwtService) {
        this.meetingService = meetingService;
        this.personRepository = personRepository;
        this.jwtService = jwtService;
    }
    /**
     * Creates a new meeting.
     *
     * @param meeting the meeting object to create
     * @return the response entity with the created meeting's ID if successful, or an error response
     */
    @Override
    @PostMapping("/create-meeting")
    public ResponseEntity<String> createMeeting(@RequestBody Meeting meeting){
        meetingService.save(meeting); // .save returns the saved meeting with a generated id
        return ResponseEntity.status(HttpStatus.CREATED).body(meeting.getId());
    }
    /**
     * Deletes a meeting with the specified meeting ID.
     *
     * @param meetingId the ID of the meeting to delete
     * @param request   the current HTTP servlet request
     * @return the response entity indicating the deletion status or an error response
     */
    @Override
    @DeleteMapping("/delete-meeting/{meetingId}")
    public ResponseEntity<String> deleteMeeting(@PathVariable String meetingId, HttpServletRequest request) {
        Meeting meeting = meetingService.findMeetingById(meetingId);

        final String authHeader = request.getHeader("Authorization");
        var jwt = authHeader.substring(7);
        String userId = jwtService.extractUsername(jwt);

        String responsiblePersonId = meeting.getResponsiblePerson().getId();
        if (!userId.equals(responsiblePersonId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this meeting.");
        }

        if (meeting == null) {
            return ResponseEntity.notFound().build();
        }

        if (meetingService.deleteMeeting(meetingId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting not found.");
    }
    /**
     * Adds an attendee to a meeting with the specified meeting ID.
     *
     * @param meetingId  the ID of the meeting to add an attendee to
     * @param attendeeId the ID of the attendee to add
     * @return the response entity indicating the operation status or an error response
     */
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
    /**
     * Removes an attendee from a meeting with the specified meeting ID.
     *
     * @param meetingId  the ID of the meeting to remove an attendee from
     * @param attendeeId the ID of the attendee to remove
     * @return the response entity indicating the operation status or an error response
     */
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
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendee not found in the meeting.");
    }
    /**
     * Retrieves a list of meetings based on the specified (optional) filters.
     *
     * @param description          the description to filter by
     * @param responsiblePersonName the responsible person's name to filter by
     * @param category             the category to filter by
     * @param type                 the type to filter by
     * @param startDate            the start date to filter by
     * @param endDate              the end date to filter by
     * @param minAttendees         the minimum number of attendees to filter by
     * @param maxAttendees         the maximum number of attendees to filter by
     *
     * @return the response entity with the filtered list of meetings if successful, or an error response
     */
    
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
