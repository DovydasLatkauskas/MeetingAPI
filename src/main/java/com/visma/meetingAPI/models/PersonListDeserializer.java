package com.visma.meetingAPI.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.visma.meetingAPI.repositories.PersonRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonListDeserializer extends JsonDeserializer<List<Person>> {
    private final PersonRepository personRepository;

    public PersonListDeserializer(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        List<Person> people = new ArrayList<>();
        for (JsonNode idNode : node) {
            String personId = idNode.asText();
            Person person = personRepository.findPersonById(personId);
            if (person != null) {
                people.add(person);
            }
        }

        return people;
    }
}
