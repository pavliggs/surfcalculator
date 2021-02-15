package com.khovaylo.surf.dto.converter.impl;

import com.khovaylo.surf.dto.UserDto;
import com.khovaylo.surf.dto.converter.Converter;
import com.khovaylo.surf.model.User;
import com.khovaylo.surf.repository.UserRepository;
import com.khovaylo.surf.service.GetService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Pavel Khovaylo
 */
@Component
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDtoConverter implements Converter<UserDto, User> {

    @Override
    public User toModel(UserDto dto) {
        if (dto == null) return null;

        return new User(dto.getId(), dto.getUserName(), dto.getPassword(), null);
    }

    @Override
    public UserDto toDto(User model) {
        if (model == null) return null;
        
        return new UserDto(model.getId(), model.getUserName(), model.getPassword());
    }
}