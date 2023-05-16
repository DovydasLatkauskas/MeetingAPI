package com.visma.meetingAPI.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.meetingAPI.repositories.PersonRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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

        return personRepository.findPersonById(personId);
    }
}
