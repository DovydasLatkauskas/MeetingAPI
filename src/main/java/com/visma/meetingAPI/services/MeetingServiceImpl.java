package com.visma.meetingAPI.services;

import com.visma.meetingAPI.models.Meeting;
import com.visma.meetingAPI.models.Person;
import com.visma.meetingAPI.repositories.MeetingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingServiceImpl implements MeetingService{
    private final MeetingRepository meetingRepository;
    public MeetingServiceImpl(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Override
    public Meeting save(Meeting meeting) {
        meeting.setId(generateMeetingId());
        meetingRepository.save(meeting);
        return meeting;
    }

    private String generateMeetingId() {
        // UUIDs are extremely unlikely to collide, so we do not check for uniqueness
        // however this check can be added at the expense of performance
        return UUID.randomUUID().toString();
    }

    @Override
    public Meeting findMeetingById(String meetingId) {
        return meetingRepository.findMeetingById(meetingId);
    }

    @Override
    public boolean deleteMeeting(String meetingId) {
        return meetingRepository.deleteMeeting(meetingId);
    }

    // includes meetings that are in progress at the start date and haven't finished by the end date
    public List<Meeting> getFilteredMeetings(String description, String responsiblePersonName, String category,
                                             String type, LocalDateTime startDate, LocalDateTime endDate,
                                             Integer minAttendees, Integer maxAttendees) {
        List<Meeting> meetings = meetingRepository.getMeetings();
        List<Meeting> filteredMeetings = new ArrayList<>();

        for (Meeting meeting : meetings) {
            // Apply filters one by one
            if (description != null && !meeting.getDescription().contains(description)) {
                continue; // Skip if description filter doesn't match
            }

            if (responsiblePersonName != null && !meeting.getResponsiblePerson().getName().contains(responsiblePersonName)) {
                continue;
            }

            if (category != null && !meeting.getCategory().getValue().contains(category)) {
                continue;
            }

            if (type != null && !meeting.getType().getValue().contains(type)) {
                continue;
            }

            // this includes meetings that are in progress at the start date
            if (startDate != null && meeting.getEndDate().isBefore(startDate)) {
                continue;
            }
            // this includes meetings that are do not finish before the end date
            if (endDate != null && meeting.getStartDate().isAfter(endDate)) {
                continue;
            }

            if (minAttendees != null && meeting.getAttendees().size() < minAttendees) {
                continue;
            }

            if (minAttendees != null && meeting.getAttendees().size() > maxAttendees) {
                continue;
            }

            // All filters passed, add meeting to the result list
            filteredMeetings.add(meeting);
        }

        return filteredMeetings;
    }

    @Override
    public void addAttendee(Person attendee, String meetingId) {
        meetingRepository.addAttendee(attendee, meetingId);
    }
    @Override
    public boolean removeAttendee(String attendeeId, String meetingId) {
        return meetingRepository.removeAttendee(attendeeId, meetingId);
    }

    @Override
    public boolean isAttendeeInIntersectingMeeting(Person attendee, Meeting meeting) {
        List<Meeting> intersectingMeetings = getFilteredMeetings(
                null, null, null, null,
                meeting.getStartDate(), meeting.getEndDate(),
                null, null
        );
        for (Meeting intersectingMeeting : intersectingMeetings) {
            if (intersectingMeeting.getAttendees().contains(attendee)) {
                return true;
            }
        }
        return false;
    }
}
