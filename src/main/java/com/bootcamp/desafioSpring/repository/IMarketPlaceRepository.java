package com.bootcamp.desafioSpring.repository;

import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;

import java.util.List;

public interface IMarketPlaceRepository {

    List<ProductDTO> getProducts();

    void savePurchaseRequest(PurchaseRequestDTO purchaseRequest);

    void deletePurchaseRequest(Integer id);

    List<PurchaseRequestDTO> getPurchaseRequest();

    void saveSell(PurchaseRequestResponseDTO purchaseRequestResponse);

    List<PurchaseRequestResponseDTO> getSells();
}
