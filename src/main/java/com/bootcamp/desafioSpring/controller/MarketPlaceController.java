package com.bootcamp.desafioSpring.controller;

import com.bootcamp.desafioSpring.exceptions.*;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;
import com.bootcamp.desafioSpring.services.IMarketPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MarketPlaceController {

    @Autowired
    IMarketPlaceService marketPlaceService;


    /*
    Genero un único EndPoint para recibir las request de productos.
    Los parametros, tanto de filtrado como ordenado, son opcionales y aditivos.
    Se pueden combinar tantos filtros como haya disponible.
     */
    @GetMapping(value = "/api/v1/articles")
    public List<ProductDTO> getProducts(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam(value = "brand", required = false) String brand,
                                        @RequestParam(value = "price", required = false) Double price,
                                        @RequestParam(value = "freeShiping", required = false) Boolean freeShiping,
                                        @RequestParam(value = "prestige", required = false) Integer prestige,
                                        @RequestParam(value = "order", required = false) Integer order) throws OrderException {
        return marketPlaceService.getProducts(name, category, brand, price, freeShiping, prestige, order);
    }


    /*
    EndPoint para la recepcion de purchaseRequest.
     */
    @PostMapping(value = "/api/v1/purchase-request")
    public PurchaseRequestResponseDTO solicitud(@RequestBody PurchaseRequestDTO solicitud) throws ProductNotFoundException, NoStockException {
        return marketPlaceService.processSolicitud(solicitud);
    }

    /*
    Manejador único y general de excepciones.
     */
    @ExceptionHandler(MarketPlaceException.class)
    public ResponseEntity<ErrorDTO> handleException(MarketPlaceException exception) {
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }
}