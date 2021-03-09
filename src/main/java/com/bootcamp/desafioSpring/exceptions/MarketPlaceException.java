package com.bootcamp.desafioSpring.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
/*
Clase padre de la cual heradan las demás excepciones.
 */
public abstract class MarketPlaceException extends Exception {
    private ErrorDTO error;
    private HttpStatus status;
}
