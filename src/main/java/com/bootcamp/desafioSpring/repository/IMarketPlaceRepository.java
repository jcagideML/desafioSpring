package com.bootcamp.desafioSpring.repository;

import com.bootcamp.desafioSpring.exceptions.BadRequestException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.model.ParamsDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;

import java.util.List;

public interface IMarketPlaceRepository {

    List<ProductDTO> getProducts(ParamsDTO params) throws OrderException;

    List<ProductDTO> orderProducts(List<ProductDTO> products, Integer order) throws OrderException;

    void savePurchaseRequest(PurchaseRequestDTO purchaseRequest) throws BadRequestException;

    void deletePurchaseRequest(Integer id) throws BadRequestException;

    List<PurchaseRequestDTO> getPurchaseRequest();

    void saveSell(PurchaseRequestResponseDTO purchaseRequestResponse) throws BadRequestException;

    List<PurchaseRequestResponseDTO> getSells();
}
