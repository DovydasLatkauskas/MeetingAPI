package com.visma.meetingAPI.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.meetingAPI.models.Person;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepositoryJSON implements PersonRepository{
    private final String DEFAULT_FILE_PATH = "JSON_database/people.json";
    private final String filePath;

    public PersonRepositoryJSON() {
        this.filePath = DEFAULT_FILE_PATH;
        initializeJSON(this.filePath);
    }

    public PersonRepositoryJSON(String filePath) {
        this.filePath = filePath;
        initializeJSON(filePath);
    }

    @Override
    public Person findPersonById(String personId) {
        List<Person> people = getPeople();
        for (Person person : people) {
            if (person.getId().equals(personId)) {
                return person;
            }
        }
        return null; // Person not found
    }

    @Override
    public List<Person> getPeople() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(filePath);
            if (file.exists()) {
                // due to Java's type erasure, simply passing List<Person>.class would not work.
                // Instead, you create an anonymous subclass of TypeReference<List<Person>>
                // using an empty set of curly braces {}
                return objectMapper.readValue(file, new TypeReference<List<Person>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Person>(); // Return an empty list if the file doesn't exist or there is an error
    }

    @Override
    public void save(Person person) {
        List<Person> people = getPeople();
        Person personInDatabase = findPersonById(person.getId());
        if(personInDatabase == null){ // if it doesn't exist, add it
            people.add(person);
        } else { // if it exists then replace it with the new one
            people.remove(personInDatabase);
            people.add(person);
        }
        savePersonListAsJson(people, filePath);
    }

    private void savePersonListAsJson(List<Person> personList, String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), personList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removePerson(String personId) {
        List<Person> persons = getPeople();
        Person personToRemove = null;
        for (Person person : persons) {
            if (person.getId().equals(personId)) {
                personToRemove = person;
                break;
            }
        }
        if (personToRemove != null) {
            persons.remove(personToRemove);
            savePersonListAsJson(persons, filePath);
            return true;
        }
        return false; // Person not found
    }

    public static void initializeJSON(String filePath) {
        File file = new File(filePath);

        // Create parent directories if they don't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Create empty JSON file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Empty JSON file created: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to create empty JSON file: " + e.getMessage());
            }
        }
    }
}
