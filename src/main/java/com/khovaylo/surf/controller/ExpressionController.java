package com.khovaylo.surf.controller;

import com.khovaylo.surf.dto.ExpressionDto;
import com.khovaylo.surf.dto.LookForBetweenTwoDatesDto;
import com.khovaylo.surf.dto.converter.Converter;
import com.khovaylo.surf.exception.NotFoundException;
import com.khovaylo.surf.exception.OperationIsNotPossibleException;
import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.model.User;
import com.khovaylo.surf.service.*;
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

    GetService<Long, User> userGetService;

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

    @GetMapping("{user_id}/sum")
    public ResponseEntity<Double> sum(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2, @NotNull @PathVariable Long user_id) {
        try {
            User user = userGetService.get(user_id);
            String expression = val1 + "+" + val2;
            Double result = calculator.sum(val1, val2);
            Expression model = new Expression(null, expression, result, null, user);
            expressionCreateService.create(model);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ArithmeticException | NotFoundException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("{user_id}/diff")
    public ResponseEntity<Double> diff(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2, @NotNull @PathVariable Long user_id) {
        try {
            User user = userGetService.get(user_id);
            String expression = val1 + "-" + val2;
            Double result = calculator.diff(val1, val2);
            Expression model = new Expression(null, expression, result, null, user);
            expressionCreateService.create(model);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ArithmeticException | NotFoundException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("{user_id}/mul")
    public ResponseEntity<Double> mul(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2, @NotNull @PathVariable Long user_id) {
        try {
            User user = userGetService.get(user_id);
            String expression = val1 + "*" + val2;
            Double result = calculator.mul(val1, val2);
            Expression model = new Expression(null, expression, result, null, user);
            expressionCreateService.create(model);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ArithmeticException | NotFoundException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }

    @GetMapping("{user_id}/div")
    public ResponseEntity<Double> div(@NotNull @RequestParam Double val1, @NotNull @RequestParam Double val2, @NotNull @PathVariable Long user_id) {
        try {
            User user = userGetService.get(user_id);
            String expression = val1 + "/" + val2;
            Double result = calculator.div(val1, val2);
            Expression model = new Expression(null, expression, result, null, user);
            expressionCreateService.create(model);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ArithmeticException | NotFoundException ex) {
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
        } catch (ArithmeticException | NotFoundException ex) {
            throw new OperationIsNotPossibleException(ex.getMessage());
        }
    }
}