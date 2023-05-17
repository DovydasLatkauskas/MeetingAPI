package com.visma.meetingAPI.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.meetingAPI.repositories.PersonRepository;
import com.visma.meetingAPI.repositories.PersonRepositoryJSON;
import lombok.NoArgsConstructor;

import java.io.IOException;
@NoArgsConstructor
public class PersonDeserializer extends JsonDeserializer<Person> {
    private PersonRepository personRepository;
    public PersonDeserializer(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        String personId = node.asText();
        PersonRepository personRepository = new PersonRepositoryJSON();

        return personRepository.findPersonById(personId);
    }
}
