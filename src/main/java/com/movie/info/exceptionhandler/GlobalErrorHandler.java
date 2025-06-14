package com.movie.info.exceptionhandler;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
public class GlobalErrorHandler {
	private final static Logger log = Logger.getLogger(GlobalErrorHandler.class.getName());

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
		log.info("Exception caught in handleRequestBodyError : {}" + ex.getMessage() + ex);

		var error = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
				.sorted().collect(Collectors.joining(" , "));
		log.info("Error is : {} " + error);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
