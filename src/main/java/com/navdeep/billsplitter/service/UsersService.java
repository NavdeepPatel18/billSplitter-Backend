package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.auth.AuthenticationRequest;
import com.navdeep.billsplitter.auth.AuthenticationResponse;
import com.navdeep.billsplitter.auth.RegisterRequest;
import com.navdeep.billsplitter.config.JwtService;
import com.navdeep.billsplitter.dto.UserDTO;
import com.navdeep.billsplitter.entity.Role;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.navdeep.billsplitter.dto.UserDTO.getUserDTO;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Users.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .role(Role.USER)
                .build();

        usersRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



    public UserDTO getUserByUsername(String username) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return getUserDTO(user);
    }



    public UserDTO updateUserByUsername(String username, UserDTO userDTO) {

        var user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if(!Objects.equals(user.getUsername(), userDTO.getUsername()) && usersRepository.existsByUsername(user.getUsername())) {
            throw new UsernameNotFoundException(username);
        }

        if(!Objects.equals(user.getEmail(), userDTO.getEmail()) && usersRepository.existsByEmail(user.getEmail())) {
            throw new UsernameNotFoundException(username);
        }

        if(!Objects.equals(user.getPhone(), userDTO.getPhone()) && usersRepository.existsByPhone(user.getPhone())) {
            throw new UsernameNotFoundException(username);
        }

        try {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setPhone(userDTO.getPhone());
            user.setUpdatedAt(LocalDateTime.now());

            Users updatedUser  = usersRepository.save(user);

            return getUserDTO(updatedUser);
        }catch (Exception e) {
            throw new UsernameNotFoundException(username);
        }

    }


}
