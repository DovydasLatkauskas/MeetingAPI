package com.visma.meetingAPI.authetication.services;

import com.visma.meetingAPI.authetication.AuthenticationRequest;
import com.visma.meetingAPI.authetication.AuthenticationResponse;
import com.visma.meetingAPI.authetication.RegisterRequest;
import com.visma.meetingAPI.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.visma.meetingAPI.models.Person;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        Person person = Person.builder().id(Person.generateId()).name(request.getName())
                .password(passwordEncoder.encode(request.getPassword())).build();
        personRepository.save(person);
        String jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder().token(jwtToken).id(person.getId()).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getId(), request.getPassword()));
        Person person = personRepository.findPersonById(request.getId());
        String jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder().token(jwtToken).id(person.getId()).build();
    }
}
