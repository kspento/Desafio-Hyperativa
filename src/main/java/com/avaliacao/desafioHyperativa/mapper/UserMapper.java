package com.avaliacao.desafioHyperativa.mapper;

import com.avaliacao.desafioHyperativa.dto.CreateUserDTO;
import com.avaliacao.desafioHyperativa.dto.UserDTO;
import com.avaliacao.desafioHyperativa.model.User;
import com.avaliacao.desafioHyperativa.model.enums.RoleType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    public UserDTO entityToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    public User createUserDTOtoEntity(CreateUserDTO createUserDTO) {
        if (createUserDTO == null) {
            return null;
        }

        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());

        return user;
    }
}
