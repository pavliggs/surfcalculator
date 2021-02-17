package com.khovaylo.surf.exception.handler;

import com.khovaylo.surf.exception.NotFoundException;
import com.khovaylo.surf.exception.OperationIsNotPossibleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Методы обработки исключений
 *
 * @author Pavel Khovaylo
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Метод обработки исключений, полученных при валидации полей.
     * Например, поле пустое, а должно быть заполнено,
     * или передаваемый email не соответствует синтаксису адреса электронной почты
     *
     * @param ex      Это исключение возникает, когда аргумент, помеченный как @Valid, не прошел проверку
     * @param headers Структура данных, представляющая заголовки HTTP-запроса или ответа,
     *                сопоставляющая имена заголовков String со списком значений String,
     *                а также предлагающая средства доступа для общих типов данных уровня приложения.
     * @param status  Перечисление кодов состояния HTTP.
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    /**
     * Метод обработки исключений, полученных при нарушении ограничений.
     * Например, id в строке запроса не соответствует @Min(1) или @Max(Long.MAX_VALUE)
     *
     * @param ex      Сообщает о результате нарушения ограничений.
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(
                    violation.getRootBeanClass().getName() + " " +
                            violation.getPropertyPath() + ": " + violation.getMessage()
            );
        }
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Метод обработки исключений, полученных при несоответствии типов.
     * Например, ожидается Long, а получен String
     *
     * @param ex      Это исключение возникает, когда аргумент метода не является ожидаемым типом
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Метод обработки исключения, если на переданный запрос не найдено обработчика.
     *
     * @param ex      По умолчанию, когда DispatcherServlet не может найти обработчик для запроса, он отправляет ответ 404.
     *                Однако, если его свойство «throwExceptionIfNoHandlerFound» имеет значение true,
     *                это исключение возникает и может обрабатываться с помощью настроенного HandlerExceptionResolver.
     * @param headers Структура данных, представляющая заголовки HTTP-запроса или ответа,
     *                сопоставляющая имена заголовков String со списком значений String,
     *                а также предлагающая средства доступа для общих типов данных уровня приложения.
     * @param status  Перечисление кодов состояния HTTP.
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatus status,
                                                                   @NonNull WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Метод обработки исключения когда в запросе отсутствует ожидаемый параметр
     *
     * @param ex      Подкласс ServletRequestBindingException, указывающий на отсутствующий параметр.
     * @param headers Структура данных, представляющая заголовки HTTP-запроса или ответа,
     *                сопоставляющая имена заголовков String со списком значений String,
     *                а также предлагающая средства доступа для общих типов данных уровня приложения.
     * @param status  Перечисление кодов состояния HTTP.
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          @NonNull HttpHeaders headers,
                                                                          @NonNull HttpStatus status,
                                                                          @NonNull WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Метод обработки исключения, получаемого если запрос содержит неподдерживаемый тип мультимедиа
     *
     * @param ex      Исключение возникает, когда клиент отправляет POST, PUT или PATCH содержимое типа,
     *                не поддерживаемого обработчиком запросов.
     * @param headers Структура данных, представляющая заголовки HTTP-запроса или ответа,
     *                сопоставляющая имена заголовков String со списком значений String,
     *                а также предлагающая средства доступа для общих типов данных уровня приложения.
     * @param status  Перечисление кодов состояния HTTP.
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     @NonNull HttpHeaders headers,
                                                                     @NonNull HttpStatus status,
                                                                     @NonNull WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ApiError apiError = new ApiError(
                LocalDateTime.now(), HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2)
        );

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Метод обработки всех остальных исключений, для которых нет отдельного обработчика
     *
     * @param ex      Класс Exception и его подклассы представляют собой форму Throwable, указывающую условия,
     *                которые приложение может поймать.
     * @param request Общий интерфейс для веб-запроса. В основном предназначен для общих перехватчиков веб-запросов,
     *                предоставляя им доступ к общим метаданным запроса, а не для фактической обработки запроса.
     * @return тело ошибки
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred"
        );
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /* Custom handlers */

    /**
     * Метод обработки исключения NotFoundException
     *
     * @param ex исключение NotFoundException
     * @return сообщение об ошибке
     */
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        ApiError errors = new ApiError(
                LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), ex.getLocalizedMessage()
        );
        return new ResponseEntity<>(errors, errors.getStatus());
    }

    /**
     * Метод обработки исключения OperationIsNotPossibleException
     *
     * @param ex исключение OperationIsNotPossibleException
     * @return сообщение об ошибке
     */
    @ExceptionHandler(OperationIsNotPossibleException.class)
    protected ResponseEntity<ApiError> handleOperationIsNotPossible(OperationIsNotPossibleException ex) {
        ApiError errors = new ApiError(
                LocalDateTime.now(), HttpStatus.NOT_FOUND, ex.getMessage(), ex.getLocalizedMessage()
        );
        return new ResponseEntity<>(errors, errors.getStatus());
    }
}
