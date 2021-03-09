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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Qualifier("marketPlaceService")
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
    public PurchaseRequestResponseDTO processPurchaseRequest(PurchaseRequestDTO request) throws ProductNotFoundException, NoStockException {
        List<ProductDTO> productDTOS;
        List<ArticleDTO> articles = processShoppingCart(request.getArticles());
        TicketDTO ticket = new TicketDTO();
        double aux = 0.0;
        boolean productExist = false;
        try {
            productDTOS = getProducts(null, null, null, null, null, null, null);
            for (ArticleDTO a : articles) {
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

        repository.savePurchaseRequest(request);
        ticket.setSolicitudId(repository.getPurchaseRequest().size());
        ticket.setArticles(articles);
        ticket.setCost(aux);
        PurchaseRequestResponseDTO purchaseRequestResponse = new PurchaseRequestResponseDTO();
        purchaseRequestResponse.setTicket(ticket);
        purchaseRequestResponse.setStatus(HttpStatus.OK);
        return purchaseRequestResponse;
    }

    private List<ArticleDTO> processShoppingCart(List<ArticleDTO> request) {
        List<ArticleDTO> articles = new ArrayList<>();

        for (ArticleDTO ar : request) {
            ArticleDTO article = new ArticleDTO();
            article.setName(ar.getName());
            article.setBrand(ar.getBrand());
            article.setQuantity(ar.getQuantity());
            article.setProductId(ar.getProductId());
            articles.add(article);
        }
        boolean containsArticle = false;
        for (PurchaseRequestDTO pr : repository.getPurchaseRequest()) {
            for (ArticleDTO prArticle : pr.getArticles()) {
                for (ArticleDTO newRequestArticle : articles) {
                    if (newRequestArticle.getProductId().equals(prArticle.getProductId())) {
                        int cant = newRequestArticle.getQuantity() + prArticle.getQuantity();
                        newRequestArticle.setQuantity(cant);
                        containsArticle = true;
                    }
                }
                if (!containsArticle) {
                    articles.add(prArticle);
                }
                containsArticle = false;
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
}