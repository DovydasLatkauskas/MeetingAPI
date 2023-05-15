package com.visma.meetingAPI.models;

import java.time.LocalDateTime;
import java.util.List;

public class Meeting {
    private String id; // UUID
    private String name;
    private Person responsiblePerson;
    private List<Person> attendees;
    private String description;
    private MeetingCategory category;
    private MeetingType meetingType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Meeting(String name, Person responsiblePerson, String description, MeetingCategory category,
                   MeetingType meetingType, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.meetingType = meetingType;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public String toString(){
        // TODO implement meeting.toString()
        return "";
    }
}
