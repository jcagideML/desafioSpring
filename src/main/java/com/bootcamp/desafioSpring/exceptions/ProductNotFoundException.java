package com.bootcamp.desafioSpring.exceptions;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends MarketPlaceException {


    /*
    Excepción que se lanza cuando el id de un producto en la purchaseRequest
    no coincide con ninguno de los de la base de datos.
     */
    public ProductNotFoundException() {
        ErrorDTO error = new ErrorDTO();
        error.setName("No se halló el producto.");
        error.setDescription("El producto no se encuentra en la base de datos.");
        this.setError(error);
        this.setStatus(HttpStatus.NOT_FOUND);
    }
}
