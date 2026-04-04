package nl.ticketsystem.mapper;

import nl.ticketsystem.dto.user.UserRequestDTO;
import nl.ticketsystem.dto.user.UserResponseDTO;
import nl.ticketsystem.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper implements DTOMapper<UserResponseDTO, UserRequestDTO, User> {

    @Override
    public UserResponseDTO mapToDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public List<UserResponseDTO> mapToDto(List<User> users) {
        return users.stream()
                .map(this::mapToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public User mapToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
