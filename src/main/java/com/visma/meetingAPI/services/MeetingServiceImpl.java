package com.visma.meetingAPI.services;

import com.visma.meetingAPI.models.Meeting;
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
        // TODO implement
        return null;
    }

    private String generateMeetingId() {
        // UUIDs are extremely unlikely to collide, so we do not check for uniqueness
        // however this check can be added at the expense of performance
        return UUID.randomUUID().toString();
    }

    @Override
    public Meeting findMeetingById(String meetingId) {
        // TODO implement
        return null;
    }

    @Override
    public boolean deleteMeeting(String meetingId) {
        // TODO implement
        return false;
    }

    @Override
    public void updateMeeting(Meeting meeting) {
        // TODO implement
    }

    public List<Meeting> getFilteredMeetings(String description, String responsiblePerson, String category,
                                             String type, LocalDateTime startDate, LocalDateTime endDate,
                                             Integer minAttendees, Integer maxAttendees) {
        List<Meeting> meetings = meetingRepository.getMeetings();
        List<Meeting> filteredMeetings = new ArrayList<>();

        for (Meeting meeting : meetings) {
            // Apply filters one by one
            if (description != null && !meeting.getDescription().contains(description)) {
                continue; // Skip if description filter doesn't match
            }

            if (responsiblePerson != null && !meeting.getResponsiblePerson().equals(responsiblePerson)) {
                continue;
            }

            if (category != null && !meeting.getCategory().equals(category)) {
                continue;
            }

            if (type != null && !meeting.getType().equals(type)) {
                continue;
            }

            if (startDate != null && meeting.getDate().isBefore(startDate)) {
                continue;
            }

            if (endDate != null && meeting.getDate().isAfter(endDate)) {
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
}
