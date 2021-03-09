package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.NoStockException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.exceptions.ProductNotFoundException;
import com.bootcamp.desafioSpring.model.ArticleDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;
import com.bootcamp.desafioSpring.repository.IMarketPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MarketPlaceServiceImpl implements IMarketPlaceService {

    @Autowired
    private final IMarketPlaceRepository repository;

    public MarketPlaceServiceImpl(IMarketPlaceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductDTO> getProducts(String name, String category, String brand, Double price, Boolean freeShiping, Integer prestige, Integer order) throws OrderException {
        List<ProductDTO> products = new ArrayList<>(repository.getProducts());

        if (name != null) {
            products.removeIf(p -> !p.getName().equals(name));
        }
        if (category != null) {
            products.removeIf(p -> !p.getCategory().equals(category));
        }
        if (brand != null) {
            products.removeIf(p -> !p.getBrand().equals(brand));
        }
        if (price != null) {
            products.removeIf(p -> !p.getPrice().equals(price));
        }
        if (freeShiping != null) {
            products.removeIf(p -> !p.getFreeShiping().equals(freeShiping));
        }
        if (prestige != null) {
            products.removeIf(p -> !p.getPrestige().equals(prestige));
        }

        if (order != null) {
            products = this.orderProducts(products, order);
        }
        return products;
    }

    @Override
    public List<ProductDTO> orderProducts(List<ProductDTO> products, Integer order) throws OrderException {

        Comparator<ProductDTO> productComparator = null;

        switch (order) {
            case 0:
                productComparator = Comparator.comparing(ProductDTO::getName, Comparator.naturalOrder());
                break;
            case 1:
                productComparator = Comparator.comparing(ProductDTO::getName, Comparator.reverseOrder());
                break;
            case 2:
                productComparator = Comparator.comparing(ProductDTO::getPrice, Comparator.reverseOrder());
                break;
            case 3:
                productComparator = Comparator.comparing(ProductDTO::getPrice, Comparator.naturalOrder());
                break;
        }
        if (productComparator != null) {
            products.sort(productComparator);
            return products;
        } else {
            throw new OrderException();
        }
    }

    @Override
    public PurchaseRequestResponseDTO processSolicitud(PurchaseRequestDTO solicitud) throws ProductNotFoundException, NoStockException {
        List<ProductDTO> productDTOS = null;
        PurchaseRequestResponseDTO response = new PurchaseRequestResponseDTO();
        double aux = 0.0;
        boolean productExist = false;
        try {
            productDTOS = getProducts(null, null, null, null, null, null, null);
            for (ArticleDTO a : solicitud.getArticles()) {
                for (ProductDTO p : productDTOS) {
                    if (a.getProductId().equals(p.getProductId())) {
                        if (a.getQuantity() <= p.getQuantity()) { //Control de stock. Que la cantidad solicitada sea menor que la que existe.
                            aux = aux + a.getQuantity() * p.getPrice();
                        } else {
                            throw new NoStockException(a);
                        }
                        productExist = true;
                    }
                }
                if (productExist) {
                    productExist = false;
                } else {
                    throw new ProductNotFoundException();
                }
            }
        } catch (OrderException e) {
            e.printStackTrace();
        }
        repository.savePurchaseRequest(solicitud);
        response.setSolicitudId(repository.getPurchaseRequest().size());
        response.setArticles(solicitud.getArticles());
        response.setCost(aux);

        return response;
    }
}