package com.visma.meetingAPI.repositories;

import com.visma.meetingAPI.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryJSONTest {
    private PersonRepositoryJSON personRepository;

    @BeforeEach
    void setUp() {
        // Create a test-specific JSON file
        String testFilePath = "src/test/test_database/test_people.json";
        personRepository = new PersonRepositoryJSON(testFilePath);
    }

    @Test
    void saveAndFindPersonById() {
        Person person = new Person("1", "John Doe", "1234", null);
        personRepository.save(person);
        Person foundPerson = personRepository.findPersonById(person.getId());

        assertNotNull(foundPerson);
        assertEquals(person.getId(), foundPerson.getId());
        assertEquals(person.getName(), foundPerson.getName());
    }

    @Test
    void getPeople() {
        Person person1 = new Person("1", "John Doe", "1234", null);
        Person person2 = new Person("2", "Jane Smith", "3456", null);

        personRepository.save(person1);
        personRepository.save(person2);

        List<Person> people = personRepository.getPeople();

        assertNotNull(people);
        assertTrue(people.contains(person1));
        assertTrue(people.contains(person2));
    }

    @Test
    void removePerson() {
        Person person = new Person("1", "John Doe", "1234", null);
        personRepository.save(person);
        boolean removed = personRepository.removePerson(person.getId());

        assertTrue(removed);
        Person foundPerson = personRepository.findPersonById(person.getId());
        assertNull(foundPerson);
    }
}

