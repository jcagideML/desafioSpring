package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.BadRequestException;
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
    public List<ProductDTO> getProducts(ParamsDTO params) throws OrderException {
        return repository.getProducts(params);
    }

    @Override
    public PurchaseRequestResponseDTO processPurchaseRequest(PurchaseRequestDTO request) throws ProductNotFoundException, NoStockException, BadRequestException {
        List<ProductDTO> productDTOS;

        acumulativeMap.clear();
        this.acumulate(request.getArticles());
        for (PurchaseRequestDTO pr : repository.getPurchaseRequest()) {
            this.acumulate(pr.getArticles());
        }

        TicketDTO ticket = new TicketDTO();
        PurchaseRequestResponseDTO purchaseRequestResponse = null;
        try {
            productDTOS = getProducts(new ParamsDTO());
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
    public void deletePurchaseRequest(Integer id) throws BadRequestException {
        repository.deletePurchaseRequest(id);
    }

    @Override
    public List<PurchaseRequestDTO> getPurchaseRequests() {
        return repository.getPurchaseRequest();
    }

    @Override
    public PurchaseRequestResponseDTO finishBuy() throws ProductNotFoundException, NoStockException, BadRequestException {
        List<ProductDTO> productDTOS;
        acumulativeMap.clear();
        for (PurchaseRequestDTO pr : repository.getPurchaseRequest()) {
            this.acumulate(pr.getArticles());
        }
        TicketDTO ticket = new TicketDTO();
        PurchaseRequestResponseDTO purchaseRequestResponse = null;
        try {
            productDTOS = getProducts(new ParamsDTO());
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