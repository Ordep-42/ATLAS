package br.ufrn.pedrogalvao.atlas.exception;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFound(
	        ResourceNotFoundException ex) {

	    ApiError error = new ApiError(
	    		Instant.now(),
	            HttpStatus.NOT_FOUND.value(),
	            HttpStatus.NOT_FOUND.getReasonPhrase(),
	            ex.getMessage());

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
		ApiError error = new ApiError(
				Instant.now(),
				HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.getReasonPhrase(),
				ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<String>> handleValidation(
	        MethodArgumentNotValidException ex) {

		List<String> errors =
	            ex.getBindingResult()
	              .getFieldErrors()
	              .stream()
	              .map(error ->
	                      error.getField()
	                      + ": "
	                      + error.getDefaultMessage())
	              .toList();

	    return ResponseEntity.badRequest()
	            .body(errors);
	}
}
