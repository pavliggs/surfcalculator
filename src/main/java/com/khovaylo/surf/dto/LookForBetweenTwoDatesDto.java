package com.khovaylo.surf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Pavel Khovaylo
 */
@Valid
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LookForBetweenTwoDatesDto {

    @NotBlank
    @Size(min = 3, max = 20)
    String startDateTime;

    @NotBlank
    @Size(min = 3, max = 20)
    String finishDateTime;
}
