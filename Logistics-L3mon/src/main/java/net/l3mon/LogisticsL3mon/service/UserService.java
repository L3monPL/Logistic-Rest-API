package net.l3mon.LogisticsL3mon.service;

import net.l3mon.LogisticsL3mon.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();
}
