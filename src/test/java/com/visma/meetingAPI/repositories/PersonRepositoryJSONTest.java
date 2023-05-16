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
        // Create a test person
        Person person = new Person("1", "John Doe", "1234", null);

        // Save the person
        personRepository.save(person);

        // Find the person by ID
        Person foundPerson = personRepository.findPersonById(person.getId());

        // Assert that the found person is not null and has the correct ID and name
        assertNotNull(foundPerson);
        assertEquals(person.getId(), foundPerson.getId());
        assertEquals(person.getName(), foundPerson.getName());
    }

    @Test
    void getPeople() {
        // Create test people
        Person person1 = new Person("1", "John Doe", "1234", null);
        Person person2 = new Person("2", "Jane Smith", "3456", null);

        // Save the people
        personRepository.save(person1);
        personRepository.save(person2);

        // Get the list of people
        List<Person> people = personRepository.getPeople();

        // Assert that the list is not null and contains the saved people
        assertNotNull(people);
        assertTrue(people.contains(person1));
        assertTrue(people.contains(person2));
    }

    @Test
    void removePerson() {
        // Create a test person
        Person person = new Person("1", "John Doe", "1234", null);

        // Save the person
        personRepository.save(person);

        // Remove the person by ID
        boolean removed = personRepository.removePerson(person.getId());

        // Assert that the person was successfully removed
        assertTrue(removed);

        // Attempt to find the person by ID
        Person foundPerson = personRepository.findPersonById(person.getId());

        // Assert that the person is not found (null)
        assertNull(foundPerson);
    }
}

