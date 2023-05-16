package com.visma.meetingAPI.repositories;
import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.MeetingCategory;
import com.visma.meetingAPI.models.MeetingType;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.repositories.MeetingRepositoryJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingRepositoryJSONTest {
    private MeetingRepositoryJSON meetingRepository;

    @BeforeEach
    void setUp() {
        // Create a test-specific JSON file
        String testFilePath = "src/test/test_database/test_meetings.json";
        meetingRepository = new MeetingRepositoryJSON(testFilePath);
    }

    final String startDateString = "2023-05-17T09:00:00Z";
    final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    final LocalDateTime startDate = LocalDateTime.parse(startDateString, formatter);
    final String endDateString = "2023-05-17T09:00:00Z";
    final LocalDateTime endDate = LocalDateTime.parse(endDateString, formatter);

    @Test
    void saveAndFindMeetingById() {
        Person responsiblePerson = new Person("1", "John Doe", "1234", null);
        Person attendee = new Person("2", "Jane Smith", "3456", null);
        Meeting meeting = new Meeting("Example Meeting", responsiblePerson, List.of(attendee),
                "This is an example meeting", MeetingCategory.HUB, MeetingType.LIVE, startDate, endDate);
        meetingRepository.save(meeting);

        Meeting foundMeeting = meetingRepository.findMeetingById(meeting.getId());

        assertNotNull(foundMeeting);
        assertEquals(meeting.getId(), foundMeeting.getId());
        assertEquals(meeting.getName(), foundMeeting.getName());
        assertEquals(meeting.getResponsiblePerson().getId(), foundMeeting.getResponsiblePerson().getId());
        assertEquals(meeting.getAttendees().size(), foundMeeting.getAttendees().size());
        assertEquals(meeting.getAttendees().get(0).getId(), foundMeeting.getAttendees().get(0).getId());
    }

    @Test
    void getMeetings() {
        Person responsiblePerson1 = new Person("1", "John Doe", "1234", null);
        Person attendee1 = new Person("2", "Jane Smith", "3456", null);
        Person responsiblePerson2 = new Person("3", "Alice Johnson", "5678", null);
        Person attendee2 = new Person("4", "Bob Anderson", "7890", null);

        Meeting meeting1 = new Meeting("Meeting 1", responsiblePerson1, List.of(attendee1),
                "Meeting 1 description", MeetingCategory.HUB, MeetingType.LIVE, startDate, endDate);
        Meeting meeting2 = new Meeting("Meeting 2", responsiblePerson2, List.of(attendee2),
                "Meeting 2 description", MeetingCategory.HUB, MeetingType.LIVE, startDate, endDate);

        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);

        List<Meeting> meetings = meetingRepository.getMeetings();

        assertNotNull(meetings);
        assertTrue(meetings.contains(meeting1));
        assertTrue(meetings.contains(meeting2));
    }

    @Test
    void removeMeeting() {
        Person responsiblePerson = new Person("1", "John Doe", "1234", null);
        Person attendee = new Person("2", "Jane Smith", "3456", null);
        Meeting meeting = new Meeting("Example Meeting", responsiblePerson, List.of(attendee),
                "This is an example meeting", MeetingCategory.HUB, MeetingType.LIVE, startDate, endDate);
        meetingRepository.save(meeting);

        boolean removed = meetingRepository.deleteMeeting(meeting.getId());

        assertTrue(removed);
        Meeting foundMeeting = meetingRepository.findMeetingById(meeting.getId());
        assertNull(foundMeeting);
    }

    @Test
    void addAttendee() {
        Person responsiblePerson = new Person("1", "John Doe", "1234", null);
        Person attendee = new Person("2", "Jane Smith", "3456", null);
        Meeting meeting = new Meeting("Example Meeting", responsiblePerson, new ArrayList<>(),
                "This is an example meeting", MeetingCategory.HUB, MeetingType.LIVE, startDate, endDate);
        meetingRepository.save(meeting);
        meetingRepository.addAttendee(attendee, meeting.getId());

        Meeting updatedMeeting = meetingRepository.findMeetingById(meeting.getId());
        assertNotNull(updatedMeeting);
        assertEquals(1, updatedMeeting.getAttendees().size());
        assertEquals(attendee.getId(), updatedMeeting.getAttendees().get(0).getId());
    }

    @Test
    void removeAttendee() {
        Person responsiblePerson = new Person("1", "John Doe", "1234", null);
        Person attendee1 = new Person("2", "Jane Smith", "3456", null);
        Person attendee2 = new Person("3", "Alice Johnson", "5678", null);
        Meeting meeting = new Meeting("Example Meeting", responsiblePerson,
                List.of(attendee1, attendee2), "This is an example meeting", MeetingCategory.HUB,
                MeetingType.LIVE, startDate, null);
        meetingRepository.save(meeting);

        boolean removed = meetingRepository.removeAttendee(attendee1.getId(), meeting.getId());

        assertTrue(removed);
        Meeting updatedMeeting = meetingRepository.findMeetingById(meeting.getId());
        assertNotNull(updatedMeeting);
        assertEquals(1, updatedMeeting.getAttendees().size());
        assertEquals(attendee2.getId(), updatedMeeting.getAttendees().get(0).getId());
    }
}