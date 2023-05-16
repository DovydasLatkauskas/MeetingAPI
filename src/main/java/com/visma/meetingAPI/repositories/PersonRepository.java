package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Person;

import java.util.List;

public interface PersonRepository {
    Person findPersonById(String attendeeId);

    List<Person> getPeople();

    void save(Person person);

    boolean removePerson(String personId);
}
