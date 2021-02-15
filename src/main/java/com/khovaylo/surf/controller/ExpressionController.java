package com.khovaylo.surf.controller;

import com.khovaylo.surf.dto.ExpressionDto;
import com.khovaylo.surf.dto.LookForBetweenTwoDatesDto;
import com.khovaylo.surf.dto.converter.Converter;
import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.service.SpecialServiceExpression;
import com.khovaylo.surf.service.util.Calculation;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Khovaylo
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/expression")
public class ExpressionController {

    Converter<ExpressionDto, Expression> expressionConverter;

    Calculation calculation;

    SpecialServiceExpression specialServiceExpression;

    GetListService<Expression> expressionGetListService;

    CreateService<Expression> expressionCreateService;

    @GetMapping("/list")
    public ResponseEntity<List<ExpressionDto>> getAll() {
        List<ExpressionDto> dtoList = expressionGetListService.getAll().stream()
                .map(expressionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/listbyvalue")
    public ResponseEntity<List<ExpressionDto>> getAllByValue(@NotNull @RequestBody String value) {
        List<ExpressionDto> dtoList = specialServiceExpression.getAllByValue(value).stream()
                .map(expressionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/listbycreatedbetweentwodates")
    public ResponseEntity<List<ExpressionDto>> getAllByCreatedBetweenTwoDates(@NotNull @Valid @RequestBody LookForBetweenTwoDatesDto period) {
        ZonedDateTime zdtStart = specialServiceExpression.getZDT(period.getStartDateTime());
        ZonedDateTime zdtFinish = specialServiceExpression.getZDT(period.getFinishDateTime());
        List<ExpressionDto> dtoList = specialServiceExpression.getAllByCreatedBetweenTwoDates(zdtStart, zdtFinish).stream()
                .map(expressionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> expression(@NotNull @Valid @RequestBody ExpressionDto dto) {
        Expression model = expressionConverter.toModel(dto);
        Double result = calculation.calculate(model.getValue());
        model.setResult(result);
        expressionCreateService.create(model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}