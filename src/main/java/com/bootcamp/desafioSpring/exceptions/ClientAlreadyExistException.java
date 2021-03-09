package com.bootcamp.desafioSpring.exceptions;

import org.springframework.http.HttpStatus;

public class ClientAlreadyExistException extends ClientException {

    public ClientAlreadyExistException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("El cliente ya existe.");
        error.setDescription("El cliente ya se encuentra registrado en la base de datos.");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
