package com.example.spring.exceptions;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExternalFileNotFoundException extends FileNotFoundException {
	private static final long serialVersionUID = -6488694278887980481L;

	public ExternalFileNotFoundException() {
		super();
	}

	public ExternalFileNotFoundException(String s) {
		super(s);
	}

}
