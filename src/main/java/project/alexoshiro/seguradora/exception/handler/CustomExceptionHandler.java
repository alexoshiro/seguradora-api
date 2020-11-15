package project.alexoshiro.seguradora.exception.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mongodb.MongoTimeoutException;

import project.alexoshiro.seguradora.dto.StatusDTO;
import project.alexoshiro.seguradora.util.MessageUtils;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		StatusDTO body = new StatusDTO(String.valueOf(status.value()), errors);

		return new ResponseEntity<>(body, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<>();

		errors.add(ex.getLocalizedMessage());

		StatusDTO body = new StatusDTO(String.valueOf(status.value()), errors);

		return new ResponseEntity<>(body, headers, status);
	}

	@ExceptionHandler(MongoTimeoutException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	protected ResponseEntity<Object> handleMongoTimeoutException(MongoTimeoutException ex, WebRequest request) {

		List<String> errors = new ArrayList<>();

		errors.add(MessageUtils.DATABASE_CONNECTION_FAIL);

		StatusDTO body = new StatusDTO(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
				errors);

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
