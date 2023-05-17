package com.visma.meetingAPI.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.visma.meetingAPI.repositories.PersonRepository;
import com.visma.meetingAPI.repositories.PersonRepositoryJSON;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class PersonListDeserializer extends JsonDeserializer<List<Person>> {
    private PersonRepository personRepository;

    public PersonListDeserializer(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        List<Person> people = new ArrayList<>();
        for (JsonNode idNode : node) {
            String personId = idNode.asText();
            PersonRepository personRepository = new PersonRepositoryJSON();
            Person person = personRepository.findPersonById(personId);
            if (person != null) {
                people.add(person);
            }
        }

        return people;
    }
}
