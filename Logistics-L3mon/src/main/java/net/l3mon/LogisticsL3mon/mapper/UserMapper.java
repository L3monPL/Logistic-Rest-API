package net.l3mon.LogisticsL3mon.mapper;

import net.l3mon.LogisticsL3mon.dto.UserDto;
import net.l3mon.LogisticsL3mon.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getMail(),
                user.getPhone(),
                user.getRole()
        );
    }

    public static User mapToUser(UserDto userDto){
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getMail(),
                userDto.getPhone(),
                userDto.getRole()
        );
    }
}
