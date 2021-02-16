package com.khovaylo.surf.controller;

import com.khovaylo.surf.dto.ExpressionDto;
import com.khovaylo.surf.dto.LookForBetweenTwoDatesDto;
import com.khovaylo.surf.dto.converter.Converter;
import com.khovaylo.surf.exception.OperationIsNotPossibleException;
import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.service.Calculator;
import com.khovaylo.surf.service.CreateService;
import com.khovaylo.surf.service.GetListService;
import com.khovaylo.surf.service.SpecialServiceExpression;
import com.khovaylo.surf.service.util.Calculation;
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
import java.time.DateTimeException;
import java.time.ZonedDateTime;
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

    Calculator calculator;

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
        try {
            ZonedDateTime zdtStart = specialServiceExpression.getZDT(period.getStartDateTime());
            ZonedDateTime zdtFinish = specialServiceExpression.getZDT(period.getFinishDateTime());
            List<ExpressionDto> dtoList = specialServiceExpression.getAllByCreatedBetweenTwoDates(zdtStart, zdtFinish).stream()
                    .map(expressionConverter::toDto).collect(Collectors.toList());
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (DateTimeException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("/sum")
    public ResponseEntity<Double> sum(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2) {
        try {
            return new ResponseEntity<>(calculator.sum(val1, val2), HttpStatus.OK);
        } catch (ArithmeticException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("/diff")
    public ResponseEntity<Double> diff(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2) {
        try {
            return new ResponseEntity<>(calculator.diff(val1, val2), HttpStatus.OK);
        } catch (ArithmeticException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("/mul")
    public ResponseEntity<Double> mul(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2) {
        try {
            return new ResponseEntity<>(calculator.mul(val1, val2), HttpStatus.OK);
        } catch (ArithmeticException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("/div")
    public ResponseEntity<Double> div(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2) {
        try {
            return new ResponseEntity<>(calculator.div(val1, val2), HttpStatus.OK);
        } catch (ArithmeticException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> expression(@NotNull @Valid @RequestBody ExpressionDto dto) {
        try {
            Expression model = expressionConverter.toModel(dto);
            Double result = calculation.calculate(model.getValue());
            model.setResult(result);
            expressionCreateService.create(model);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ArithmeticException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }
}