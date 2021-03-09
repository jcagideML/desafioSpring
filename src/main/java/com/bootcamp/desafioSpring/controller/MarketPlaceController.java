package com.bootcamp.desafioSpring.controller;

import com.bootcamp.desafioSpring.exceptions.*;
import com.bootcamp.desafioSpring.model.ClientDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;
import com.bootcamp.desafioSpring.services.IClientService;
import com.bootcamp.desafioSpring.services.IMarketPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarketPlaceController {

    @Autowired
    @Qualifier("clientService")
    IClientService clientService;

    @Autowired
    @Qualifier("marketPlaceService")
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
    public PurchaseRequestResponseDTO purchaseRequest(@RequestBody PurchaseRequestDTO solicitud) throws ProductNotFoundException, NoStockException {
        return marketPlaceService.processPurchaseRequest(solicitud);
    }

    @GetMapping(value = "/api/v1/purchase-request/getAll")
    public List<PurchaseRequestDTO> getAllPurchaseRequest() {
        return this.marketPlaceService.getPurchaseRequests();
    }

    @GetMapping(value = "/api/v1/purchase-request/delete/{requestId}")
    public List<PurchaseRequestDTO> deletePurchaseRequest(@PathVariable(value = "requestId") Integer id) {
        this.marketPlaceService.deletePurchaseRequest(id);
        return this.marketPlaceService.getPurchaseRequests();
    }

    @PostMapping(value = "/api/v1/clients/newClient")
    public void newClient(@RequestBody ClientDTO client) throws MissingDataException, ClientAlreadyExistException {
        this.clientService.newClient(client);
    }

    @GetMapping(value = "/api/v1/clients/getAll")
    public List<ClientDTO> getAllClients() {
        return this.clientService.getClients();
    }

    @GetMapping(value = "/api/v1/clients/getByProvincia/{provincia}")
    public List<ClientDTO> getClientByProvincia(@PathVariable(value = "provincia") String provincia) {
        return this.clientService.getClientByProvincia(provincia);
    }

    @GetMapping(value = "/api/v1/clients/delete/{clientId}")
    public List<ClientDTO> deleteClient(@PathVariable(value = "clientId") Integer id) {
        this.clientService.deleteClient(id);
        return this.clientService.getClients();
    }

    @ExceptionHandler(MarketPlaceException.class)
    public ResponseEntity<ErrorDTO> handleException(MarketPlaceException exception) {
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorDTO> handleException(ClientException exception) {
        return new ResponseEntity<>(exception.getError(), exception.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRunTimeException(RuntimeException exception) {
        ErrorDTO error = new ErrorDTO();
        error.setName("Internal server error.");
        error.setDescription("Un error en el servidor provocó que se detuviera la ejecución. Contactese con soporte.");
        exception.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}