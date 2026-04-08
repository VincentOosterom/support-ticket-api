package nl.ticketsystem.service;

import nl.ticketsystem.dto.user.UserRequestDTO;
import nl.ticketsystem.dto.user.UserResponseDTO;
import nl.ticketsystem.exception.ResourceNotFoundException;
import nl.ticketsystem.mapper.UserMapper;
import nl.ticketsystem.model.User;
import nl.ticketsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userMapper.mapToDto(userRepository.findAll());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));
        return userMapper.mapToDto(user);
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = userMapper.mapToEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.mapToDto(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
