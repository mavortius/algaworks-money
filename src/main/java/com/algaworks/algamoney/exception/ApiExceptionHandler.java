package com.algaworks.algamoney.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  public ApiExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
          HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String message = messageSource.getMessage("message-invalid", null, LocaleContextHolder.getLocale());
    String cause = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
    List<ErrorMessage> errors = Collections.singletonList(new ErrorMessage(message, cause));
    return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<ErrorMessage> errors = build(ex.getBindingResult());
    return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({EmptyResultDataAccessException.class})
  public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
    String message = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
    String cause = ex.toString();
    List<ErrorMessage> errors = Collections.singletonList(new ErrorMessage(message, cause));
    return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler({DataIntegrityViolationException.class})
  public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
    String message = messageSource.getMessage("resource.operation-not-allowed", null, LocaleContextHolder.getLocale());
    String cause = ExceptionUtils.getRootCauseMessage(ex);
    List<ErrorMessage> errors = Collections.singletonList(new ErrorMessage(message, cause));
    return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  private List<ErrorMessage> build(BindingResult bindingResult) {
    List<ErrorMessage> errors = new ArrayList<>();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
      String cause = fieldError.toString();
      errors.add(new ErrorMessage(message, cause));
    }
    return errors;
  }

  public static class ErrorMessage {
    private final String message;
    private final String cause;

    public ErrorMessage(String message, String cause) {
      this.message = message;
      this.cause = cause;
    }

    public String getMessage() {
      return message;
    }

    public String getCause() {
      return cause;
    }
  }
}
