package com.bootcamp.desafioSpring.repository;

import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;

import java.util.List;

public interface IMarketPlaceRepository {

    List<ProductDTO> getProducts();

    void savePurchaseRequest(PurchaseRequestDTO purchaseRequest);

    void deletePurchaseRequest(PurchaseRequestDTO purchaseRequest);

    List<PurchaseRequestDTO> getPurchaseRequest();
}
