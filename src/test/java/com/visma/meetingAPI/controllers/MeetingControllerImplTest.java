package com.visma.meetingAPI.controllers;

import com.visma.meetingAPI.authetication.services.JwtService;
import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.repositories.PersonRepository;
import com.visma.meetingAPI.services.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingControllerImplTest {
    @Mock
    private MeetingService meetingService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private MeetingControllerImpl meetingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMeeting_ReturnsCreatedResponse() {
        Meeting meeting = new Meeting();
        String expectedMeetingId = "12345";
        meeting.setId(expectedMeetingId);
        when(meetingService.save(meeting)).thenReturn(meeting);

        ResponseEntity<String> response = meetingController.createMeeting(meeting);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedMeetingId, response.getBody());
        verify(meetingService, times(1)).save(meeting);
    }

    @Test
    void deleteMeeting_WithValidMeetingIdAndAuthorizedUser_ReturnsOkResponse() {
        String meetingId = "12345";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person responsiblePerson = new Person();
        responsiblePerson.setId("user123");
        meeting.setResponsiblePerson(responsiblePerson);

        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.extractUsername("token123")).thenReturn("user123");
        when(meetingService.deleteMeeting(meetingId)).thenReturn(true);

        ResponseEntity<String> response = meetingController.deleteMeeting(meetingId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(request, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername("token123");
        verify(meetingService, times(1)).deleteMeeting(meetingId);
    }

    @Test
    void deleteMeeting_WithInvalidMeetingId_ReturnsNotFoundResponse() {
        String invalidMeetingId = "invalidId";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(meetingService.findMeetingById(invalidMeetingId)).thenReturn(null);
        ResponseEntity<String> response = meetingController.deleteMeeting(invalidMeetingId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(meetingService, times(1)).findMeetingById(invalidMeetingId);
    }
    @Test
    void deleteMeeting_WithUnauthorizedUser_ReturnsForbiddenResponse() {
        String meetingId = "12345";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person responsiblePerson = new Person();
        responsiblePerson.setId("user123");
        meeting.setResponsiblePerson(responsiblePerson);
        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(jwtService.extractUsername("token123")).thenReturn("user456");
        ResponseEntity<String> response = meetingController.deleteMeeting(meetingId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(request, times(1)).getHeader("Authorization");
        verify(jwtService, times(1)).extractUsername("token123");
        verifyNoMoreInteractions(meetingService);
    }

    @Test
    void addAttendee_WithValidMeetingIdAndAttendeeId_ReturnsCreatedResponse() {
        // Arrange
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person attendee = new Person();
        attendee.setId(attendeeId);

        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(attendee);
        when(meetingService.isAttendeeInIntersectingMeeting(attendee, meeting)).thenReturn(false);
        ResponseEntity<String> response = meetingController.addAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verify(meetingService, times(1)).isAttendeeInIntersectingMeeting(attendee, meeting);
        verify(meetingService, times(1)).addAttendee(attendee, meetingId);
    }

    @Test
    void addAttendee_WithInvalidMeetingId_ReturnsNotFoundResponse() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        when(meetingService.findMeetingById(meetingId)).thenReturn(null);
        ResponseEntity<String> response = meetingController.addAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
    }

    @Test
    void addAttendee_WithInvalidAttendeeId_ReturnsNotFoundResponse() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(null);
        ResponseEntity<String> response = meetingController.addAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verifyNoMoreInteractions(meetingService);
    }

    @Test
    void addAttendee_WithAttendeeAlreadyAdded_ReturnsBadRequestResponse() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person attendee = new Person();
        attendee.setId(attendeeId);
        meeting.addAttendee(attendee);
        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(attendee);
        ResponseEntity<String> response = meetingController.addAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Attendee is already added to the meeting.", response.getBody());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verifyNoMoreInteractions(meetingService);
    }

    @Test
    void addAttendee_WithAttendeeInConflictingMeeting_ReturnsWarning() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person attendee = new Person();
        attendee.setId(attendeeId);
        List<Meeting> conflictingMeetings = new ArrayList<>();
        conflictingMeetings.add(new Meeting());

        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(attendee);
        when(meetingService.isAttendeeInIntersectingMeeting(attendee, meeting)).thenReturn(true);

        ResponseEntity<String> response = meetingController.addAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Attendee is already in a conflicting meeting.", response.getBody());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verify(meetingService, times(1)).isAttendeeInIntersectingMeeting(attendee, meeting);
        verifyNoMoreInteractions(meetingService);
    }

    @Test
    void removeAttendee_WithValidMeetingIdAndAttendeeId_ReturnsOkResponse() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person attendee = new Person();
        attendee.setId(attendeeId);

        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(attendee);
        when(meetingService.removeAttendee(attendeeId, meetingId)).thenReturn(true);

        ResponseEntity<String> response = meetingController.removeAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verify(meetingService, times(1)).removeAttendee(attendeeId, meetingId);
    }

    @Test
    void removeAttendee_WithInvalidAttendeeId_ReturnsNotFoundResponse() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);

        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(null);
        ResponseEntity<String> response = meetingController.removeAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verifyNoMoreInteractions(meetingService);
    }

    @Test
    void removeAttendee_WithAttendeeNotInMeeting_ReturnsBadRequestResponse() {
        String meetingId = "12345";
        String attendeeId = "attendee123";
        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        Person attendee = new Person();
        attendee.setId(attendeeId);

        when(meetingService.findMeetingById(meetingId)).thenReturn(meeting);
        when(personRepository.findPersonById(attendeeId)).thenReturn(attendee);
        when(meetingService.removeAttendee(attendeeId, meetingId)).thenReturn(false);
        ResponseEntity<String> response = meetingController.removeAttendee(meetingId, attendeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Attendee is not present in the meeting.", response.getBody());
        verify(meetingService, times(1)).findMeetingById(meetingId);
        verify(personRepository, times(1)).findPersonById(attendeeId);
        verify(meetingService, times(1)).removeAttendee(attendeeId, meetingId);
    }
}