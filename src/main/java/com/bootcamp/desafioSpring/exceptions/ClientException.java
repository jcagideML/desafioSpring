package com.bootcamp.desafioSpring.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class ClientException extends Exception {
    private ErrorDTO error;
    private HttpStatus status;
}

