package com.visma.meetingAPI.models;

public enum MeetingCategory {
    CODE_MONKEY("CodeMonkey"),
    HUB("Hub"),
    SHORT("Short"),
    TEAM_BUILDING("TeamBuilding");

    private final String value;

    MeetingCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
