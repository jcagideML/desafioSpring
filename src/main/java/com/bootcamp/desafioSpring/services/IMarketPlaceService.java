package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.BadRequestException;
import com.bootcamp.desafioSpring.exceptions.NoStockException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.exceptions.ProductNotFoundException;
import com.bootcamp.desafioSpring.model.ParamsDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;

import java.util.List;

public interface IMarketPlaceService {

    List<ProductDTO> getProducts(ParamsDTO params) throws OrderException;

    PurchaseRequestResponseDTO processPurchaseRequest(PurchaseRequestDTO request) throws ProductNotFoundException, NoStockException, BadRequestException;

    void deletePurchaseRequest(Integer id) throws BadRequestException;

    List<PurchaseRequestDTO> getPurchaseRequests();

    PurchaseRequestResponseDTO finishBuy() throws ProductNotFoundException, NoStockException, BadRequestException;

    List<PurchaseRequestResponseDTO> getSells();
}
