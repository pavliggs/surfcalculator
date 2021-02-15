package com.khovaylo.surf.controller;

import com.khovaylo.surf.dto.UserDto;
import com.khovaylo.surf.dto.converter.Converter;
import com.khovaylo.surf.model.User;
import com.khovaylo.surf.service.CreateService;
import com.khovaylo.surf.service.GetListService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Khovaylo
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/user")
public class UserController {

    Converter<UserDto, User> userConverter;

    GetListService<User> userGetListService;

    CreateService<User> userCreateService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> dtoList = userGetListService.getAll().stream()
                .map(userConverter::toDto).collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@NotNull @Valid @RequestBody UserDto dto) {
        User model = userConverter.toModel(dto);
        userCreateService.create(model);
    }
}
