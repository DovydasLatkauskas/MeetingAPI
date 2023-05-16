package com.visma.meetingAPI.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Meeting {
    private String id; // this is a locally generated UUID
    private String name;
    @JsonDeserialize(using = PersonDeserializer.class)
    private Person responsiblePerson;
    @JsonDeserialize(using = PersonListDeserializer.class)
    private List<Person> attendees;
    private String description;
    private MeetingCategory category;
    private MeetingType type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Meeting(String name, Person responsiblePerson, List<Person> attendees, String description, MeetingCategory category,
                   MeetingType type, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = generateId();
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.attendees = attendees;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Meeting ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Responsible Person: ").append(responsiblePerson).append("\n");

        sb.append("Attendees: ");
        for (Person attendee : attendees) {
            sb.append(attendee).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
        sb.append("\n");

        sb.append("Description: ").append(description).append("\n");
        sb.append("Category: ").append(category).append("\n");
        sb.append("Meeting Type: ").append(type).append("\n");
        sb.append("Start Date: ").append(startDate).append("\n");
        sb.append("End Date: ").append(endDate).append("\n");

        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public List<Person> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Person> attendees) {
        this.attendees = attendees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeetingCategory getCategory() {
        return category;
    }

    public void setCategory(MeetingCategory category) {
        this.category = category;
    }

    public MeetingType getType() {
        return type;
    }

    public void setType(MeetingType type) {
        this.type = type;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void addAttendee(Person attendee) {
        attendees.add(attendee);
    }
}
