package com.visma.meetingAPI.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MeetingRepositoryJSON implements MeetingRepository {
    private final String FILE_PATH = "JSON_database/meetings.json";
    @Override
    public List<Meeting> getMeetings() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(FILE_PATH);
            if (file.exists()) {
                // due to Java's type erasure, simply passing List<Meeting>.class would not work.
                // Instead, you create an anonymous subclass of TypeReference<List<Meeting>>
                // using an empty set of curly braces {}
                return objectMapper.readValue(file, new TypeReference<List<Meeting>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Meeting>(); // Return an empty list if the file doesn't exist or there is an error
    }

    @Override
    public void save(Meeting meeting) {
        List<Meeting> meetings = getMeetings();
        Meeting meetingInDatabase = findMeetingById(meeting.getId());
        if(meetingInDatabase == null){ // if it doesn't exist, add it
            meetings.add(meeting);
        } else { // if it exists then replace it with the new one
            meetings.remove(meetingInDatabase);
            meetings.add(meeting);
        }
        saveMeetingListAsJson(meetings, FILE_PATH);
    }

    private void saveMeetingListAsJson(List<Meeting> meetingList, String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), meetingList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Meeting findMeetingById(String meetingId) {
        List<Meeting> meetings = getMeetings();
        for (Meeting meeting : meetings) {
            if (meeting.getId().equals(meetingId)) {
                return meeting;
            }
        }
        return null; // Meeting not found
    }

    @Override
    public boolean deleteMeeting(String meetingId) {
        List<Meeting> meetings = getMeetings();
        Meeting meetingToRemove = null;
        for (Meeting meeting : meetings) {
            if (meeting.getId().equals(meetingId)) {
                meetingToRemove = meeting;
                break;
            }
        }
        if (meetingToRemove != null) {
            meetings.remove(meetingToRemove);
            saveMeetingListAsJson(meetings, FILE_PATH);
            return true;
        }
        return false; // Meeting not found
    }

    @Override
    public void addAttendee(Person attendee, String meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        if (meeting != null) {
            List<Person> attendees = meeting.getAttendees();
            attendees.add(attendee);
            save(meeting);
        }
    }

    @Override
    public boolean removeAttendee(String attendeeId, String meetingId) {
        Meeting meeting = findMeetingById(meetingId);
        if (meeting != null) {
            List<Person> attendees = meeting.getAttendees();
            Person attendeeToRemove = null;
            for (Person attendee : attendees) {
                if (attendee.getId().equals(attendeeId)) {
                    attendeeToRemove = attendee;
                    break;
                }
            }
            if (attendeeToRemove != null) {
                attendees.remove(attendeeToRemove);
                saveMeetingListAsJson(getMeetings(), FILE_PATH);
                return true; // Attendee removed successfully
            }
        }
        return false; // Meeting not found or attendee not found
    }
}
