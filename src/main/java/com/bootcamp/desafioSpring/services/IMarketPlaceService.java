package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.NoStockException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.exceptions.ProductNotFoundException;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;

import java.util.List;

public interface IMarketPlaceService {

    List<ProductDTO> getProducts(String name, String category, String brand, Double price, Boolean freeShiping, Integer prestige, Integer order) throws OrderException;

    List<ProductDTO> orderProducts(List<ProductDTO> products, Integer order) throws OrderException;

    PurchaseRequestResponseDTO processSolicitud(PurchaseRequestDTO solicitud) throws ProductNotFoundException, NoStockException;
}
