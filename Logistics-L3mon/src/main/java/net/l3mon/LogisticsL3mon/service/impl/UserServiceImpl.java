package net.l3mon.LogisticsL3mon.service.impl;

import lombok.AllArgsConstructor;
import net.l3mon.LogisticsL3mon.dto.UserDto;
import net.l3mon.LogisticsL3mon.entity.User;
import net.l3mon.LogisticsL3mon.exception.ResourceNotFoundException;
import net.l3mon.LogisticsL3mon.mapper.UserMapper;
import net.l3mon.LogisticsL3mon.repository.UserRepository;
import net.l3mon.LogisticsL3mon.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exist with given id: " + userId));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }
}
