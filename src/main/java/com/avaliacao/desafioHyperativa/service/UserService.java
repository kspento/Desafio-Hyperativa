package com.avaliacao.desafioHyperativa.service;

import com.avaliacao.desafioHyperativa.dto.CreateUserDTO;
import com.avaliacao.desafioHyperativa.dto.UserDTO;
import com.avaliacao.desafioHyperativa.mapper.CardMapper;
import com.avaliacao.desafioHyperativa.mapper.UserMapper;
import com.avaliacao.desafioHyperativa.model.Role;
import com.avaliacao.desafioHyperativa.model.User;
import com.avaliacao.desafioHyperativa.model.enums.RoleType;
import com.avaliacao.desafioHyperativa.repository.RoleRepository;
import com.avaliacao.desafioHyperativa.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    public void registerUser(CreateUserDTO user) {

        var userDb = userMapper.createUserDTOtoEntity(user);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        var pwd = passwordEncoder.encode(userDb.getPassword());
        userDb.setPassword(pwd);
        var registeredUser = userRepository.findByUsername(user.getUsername());

        if (registeredUser != null)
        {
            throw new DuplicateRequestException("Username already taken. Please try a different one.");
        }

        Set<RoleType> sentRoles = user.getRoles();

        Set<Role> userRoles = roleRepository.findByNameIn(sentRoles);

        Set<RoleType> existingRoleNames = userRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        List<RoleType> invalidRoles = sentRoles.stream()
                .filter(role -> !existingRoleNames.contains(role))
                .toList();

        if (!invalidRoles.isEmpty()) {
            throw new IllegalArgumentException("Invalid roles: " + invalidRoles);
        }

        userDb.setRoles(userRoles);

        userDb =  userRepository.save(userDb);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
