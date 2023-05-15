package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Person;

public interface PersonRepository {
    Person findById(String attendeeId);
}
