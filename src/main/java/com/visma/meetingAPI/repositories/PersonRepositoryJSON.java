package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Person;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryJSON implements PersonRepository{
    @Override
    public Person findPersonById(String attendeeId) {
        // TODO implement
        return null;
    }
}
