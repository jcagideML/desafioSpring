package com.bootcamp.desafioSpring.controller;

import com.bootcamp.desafioSpring.exceptions.*;
import com.bootcamp.desafioSpring.model.*;
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
        ParamsDTO params = new ParamsDTO();
        params.setName(name);
        params.setCategory(category);
        params.setBrand(brand);
        params.setPrice(price);
        params.setFreeShiping(freeShiping);
        params.setPrestige(prestige);
        params.setOrder(order);
        return marketPlaceService.getProducts(params);
    }

    /*
    EndPoint para la recepcion de purchaseRequest.
    Las request son acumulativas y tambien se pueden borrar.
    Si se borra una purchaseRequest, se actualiza el valor del carrito tambien.
     */
    @PostMapping(value = "/api/v1/purchase-request")
    public PurchaseRequestResponseDTO purchaseRequest(@RequestBody PurchaseRequestDTO solicitud) throws ProductNotFoundException, NoStockException, BadRequestException {
        return marketPlaceService.processPurchaseRequest(solicitud);
    }

    @GetMapping(value = "/api/v1/purchase-request/getAll")
    public List<PurchaseRequestDTO> getAllPurchaseRequest() {
        return this.marketPlaceService.getPurchaseRequests();
    }

    @GetMapping(value = "/api/v1/purchase-request/delete/{requestId}")
    public List<PurchaseRequestDTO> deletePurchaseRequest(@PathVariable(value = "requestId") Integer id) throws BadRequestException {
        this.marketPlaceService.deletePurchaseRequest(id);
        return this.marketPlaceService.getPurchaseRequests();
    }

    @GetMapping(value = "/api/v1/purchase-request/finish")
    public PurchaseRequestResponseDTO finishBuy() throws ProductNotFoundException, NoStockException, BadRequestException {
        return this.marketPlaceService.finishBuy();
    }

    @GetMapping(value = "/api/v1/purchase-request/getAllSells")
    public List<PurchaseRequestResponseDTO> getAllSells() {
        return this.marketPlaceService.getSells();
    }

    /*
   EndPoints para controlar request de clientes.
   Se puede dar de alta y eliminar un cliente.
   También se puede obtener todos los clientes o filtrarlos por provincia.
    */
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


    /*
    Se crean tres handler de excepciones. Se diferencia entre MarketPlace y Client.
    Por último, se atajan las runtime exception y se le avisa al usuario que ocurrió un error interno.
     */
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