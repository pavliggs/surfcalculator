package com.khovaylo.surf.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * @author Pavel Khovaylo
 */
@Valid
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpressionDto {

    @Min(0L)
    Long id;

    @NotBlank
    @Size(min = 3, max = 254)
    String value;

    @NotNull
    @Min(0L)
    Long userId;
}