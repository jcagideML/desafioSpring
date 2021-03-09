package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.NoStockException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.exceptions.ProductNotFoundException;
import com.bootcamp.desafioSpring.model.*;
import com.bootcamp.desafioSpring.repository.IMarketPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("marketPlaceService")
public class MarketPlaceServiceImpl implements IMarketPlaceService {

    @Autowired
    private final IMarketPlaceRepository repository;
    private final Map<Integer, Integer> acumulativeMap;

    public MarketPlaceServiceImpl(IMarketPlaceRepository repository) {
        this.repository = repository;
        this.acumulativeMap = new HashMap<>();
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
    public PurchaseRequestResponseDTO processPurchaseRequest(PurchaseRequestDTO request) throws ProductNotFoundException, NoStockException {
        List<ProductDTO> productDTOS;

        acumulativeMap.clear();
        this.acumulate(request.getArticles());
        for (PurchaseRequestDTO pr : repository.getPurchaseRequest()) {
            this.acumulate(pr.getArticles());
        }

        TicketDTO ticket = new TicketDTO();
        PurchaseRequestResponseDTO purchaseRequestResponse = null;
        try {
            productDTOS = getProducts(null, null, null, null, null, null, null);
            double aux = stockAndCostCalculator(productDTOS);

            repository.savePurchaseRequest(request);

            ticket.setTicketId(repository.getPurchaseRequest().size());
            ticket.setArticles(getArticlesFromDataBase(acumulativeMap, productDTOS));
            ticket.setCost(aux);

            purchaseRequestResponse = new PurchaseRequestResponseDTO();
            purchaseRequestResponse.setTicket(ticket);
            purchaseRequestResponse.setStatus(HttpStatus.OK);

        } catch (OrderException e) {
            e.printStackTrace();
        }
        return purchaseRequestResponse;
    }

    private Double stockAndCostCalculator(List<ProductDTO> productDTOS) throws ProductNotFoundException, NoStockException {
        double aux = 0.0;
        boolean productExist = false;
        for (Integer id : acumulativeMap.keySet()) {
            for (ProductDTO p : productDTOS) {
                if (p.getProductId().equals(id)) {
                    if (acumulativeMap.get(id) <= p.getQuantity()) { //Control de stock. Que la cantidad solicitada sea menor que la que existe.
                        aux = aux + acumulativeMap.get(id) * p.getPrice();
                    } else {
                        throw new NoStockException(p.getName(), p.getBrand());
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
        return aux;
    }

    private void acumulate(List<ArticleDTO> toAcumulate) {

        for (ArticleDTO acumulativeArticle : toAcumulate) {
            if (this.acumulativeMap.containsKey(acumulativeArticle.getProductId())) {
                int cant = acumulativeMap.get(acumulativeArticle.getProductId()) + acumulativeArticle.getQuantity();
                acumulativeMap.replace(acumulativeArticle.getProductId(), cant);
            } else {
                acumulativeMap.put(acumulativeArticle.getProductId(), acumulativeArticle.getQuantity());
            }
        }
    }

    public List<ArticleDTO> getArticlesFromDataBase(Map<Integer, Integer> articlesId, List<ProductDTO> products) {
        List<ArticleDTO> articles = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : articlesId.entrySet()) {
            for (ProductDTO p : products) {
                if (p.getProductId().equals(entry.getKey())) {
                    ArticleDTO art = new ArticleDTO();
                    art.setProductId(p.getProductId());
                    art.setName(p.getName());
                    art.setBrand(p.getBrand());
                    art.setQuantity(entry.getValue());
                    articles.add(art);
                }
            }
        }
        return articles;
    }

    @Override
    public void deletePurchaseRequest(Integer id) {
        repository.deletePurchaseRequest(id);
    }

    @Override
    public List<PurchaseRequestDTO> getPurchaseRequests() {
        return repository.getPurchaseRequest();
    }

    @Override
    public PurchaseRequestResponseDTO finishBuy() throws ProductNotFoundException, NoStockException {
        List<ProductDTO> productDTOS;
        acumulativeMap.clear();
        for (PurchaseRequestDTO pr : repository.getPurchaseRequest()) {
            this.acumulate(pr.getArticles());
        }
        TicketDTO ticket = new TicketDTO();
        PurchaseRequestResponseDTO purchaseRequestResponse = null;
        try {
            productDTOS = getProducts(null, null, null, null, null, null, null);
            double aux = stockAndCostCalculator(productDTOS);

            ticket.setArticles(getArticlesFromDataBase(acumulativeMap, productDTOS));
            ticket.setCost(aux);

            purchaseRequestResponse = new PurchaseRequestResponseDTO();
            purchaseRequestResponse.setTicket(ticket);
            purchaseRequestResponse.setStatus(HttpStatus.OK);
            this.repository.saveSell(purchaseRequestResponse);
            this.repository.getPurchaseRequest().clear();
        } catch (OrderException e) {
            e.printStackTrace();
        }
        return purchaseRequestResponse;
    }

    @Override
    public List<PurchaseRequestResponseDTO> getSells() {
        return repository.getSells();
    }
}