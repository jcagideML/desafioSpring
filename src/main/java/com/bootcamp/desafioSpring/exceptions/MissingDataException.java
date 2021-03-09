package com.bootcamp.desafioSpring.exceptions;

import org.springframework.http.HttpStatus;

public class MissingDataException extends ClientException {

    public MissingDataException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("Falta información del cliente.");
        error.setDescription("Algunos de los campos para dar de alta un cliente estan imcompletos.");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
