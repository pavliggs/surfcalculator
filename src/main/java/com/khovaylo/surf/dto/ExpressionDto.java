package com.khovaylo.surf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.ZonedDateTime;

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

    Double result;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @PastOrPresent
    ZonedDateTime created;

    @NotNull
    @Min(0L)
    Long userId;
}