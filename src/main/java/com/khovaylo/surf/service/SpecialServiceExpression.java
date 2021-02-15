package com.khovaylo.surf.service;

import com.khovaylo.surf.exception.NotFoundException;
import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.repository.ExpressionRepository;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Pavel Khovaylo
 */
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SpecialServiceExpression {

    ExpressionRepository expressionRepository;

    public List<Expression> getAllByValue(String value) {
        return expressionRepository.findAllByValue(value);
    }

    public ZonedDateTime getZDT(String stringDateTime) {
        String dateTimePattern = "dd.MM.yyyy HH:mm";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        LocalDateTime ldt = LocalDateTime.parse(stringDateTime, dateTimeFormatter);
        return ZonedDateTime.of(ldt, ZoneId.systemDefault());
    }

    public List<Expression> getAllByCreatedBetweenTwoDates(ZonedDateTime start, ZonedDateTime finish) {
        return expressionRepository.findByCreatedIsAfterAndCreatedIsBefore(start, finish);
    }
}
