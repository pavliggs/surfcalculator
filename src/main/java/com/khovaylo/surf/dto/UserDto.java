package com.khovaylo.surf.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Pavel Khovaylo
 */
@Valid
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @Min(0L)
    Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    String userName;

    @NotBlank
    @Size(min = 3, max = 254)
    String password;
}