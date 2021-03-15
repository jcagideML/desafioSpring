package com.bootcamp.desafioSpring;

import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.model.ParamsDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.repository.IMarketPlaceRepository;
import com.bootcamp.desafioSpring.services.MarketPlaceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class MarketPlaceServiceTest {

    @Mock
    IMarketPlaceRepository repository;

    MarketPlaceServiceImpl service;

    @BeforeEach
    void init() {
        openMocks(this);
        service = new MarketPlaceServiceImpl(repository);
    }

    @Test
    public void getAllProducts() throws OrderException {
        List<ProductDTO> products = new ArrayList<>();

        when(repository.getProducts(new ParamsDTO())).thenReturn(products);

        Assertions.assertEquals(products.size(), service.getProducts(new ParamsDTO()).size());
    }

}
