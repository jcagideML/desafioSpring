package com.bootcamp.desafioSpring.exceptions;

import com.bootcamp.desafioSpring.model.ArticleDTO;
import org.springframework.http.HttpStatus;

public class NoStockException extends MarketPlaceException {

    /*
    Excepción que se utiliza cuando el service de purchaseRequest detecta que no hay stock.
    Tiene como paramentro el artículo que lanzó la excepción para que el usuario pueda saber cuál es el que
    debe disminuir.
     */
    public NoStockException(ArticleDTO article) {
        ErrorDTO error = new ErrorDTO();
        error.setName("No hay esa cantidad del producto");
        error.setDescription("La cantidad ingresada de " + article.getName() + " marca " + article.getBrand() + " excede el stock.");
        this.setError(error);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
