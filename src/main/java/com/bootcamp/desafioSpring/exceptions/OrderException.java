package com.bootcamp.desafioSpring.exceptions;

import org.springframework.http.HttpStatus;

public class OrderException extends MarketPlaceException {

    /*
    Si el número que se pasa al endppoint para ordenar la lista de productos no se encuentra
    en el switch, lanza una excepción del tipo BAD_REQUEST.
     */
    public OrderException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("Tipo de orden no existente.");
        error.setDescription("El tipo de orden seleccionado no existe.");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
