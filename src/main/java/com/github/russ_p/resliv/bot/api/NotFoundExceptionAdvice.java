package com.github.russ_p.resliv.bot.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class NotFoundExceptionAdvice {

	@ExceptionHandler({ NotFoundException.class })
	public final ResponseEntity<String> handleException(Exception ex, WebRequest request) {
		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<>("Resource not found", headers, HttpStatus.NOT_FOUND);
	}
}
