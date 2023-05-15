package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Meeting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MeetingRepositoryJSON implements MeetingRepository {

    @Override
    public List<Meeting> getMeetings() {
        // TODO implement
        return null;
    }

    @Override
    public void save(Meeting meeting) {
        // TODO implement
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
}
