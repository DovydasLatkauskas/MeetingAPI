package com.visma.meetingAPI.models;

public enum MeetingType {
    LIVE("Live"),
    IN_PERSON("In person");

    private final String value;

    MeetingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
