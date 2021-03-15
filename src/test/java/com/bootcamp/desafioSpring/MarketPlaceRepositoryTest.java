package com.bootcamp.desafioSpring;

import com.bootcamp.desafioSpring.exceptions.BadRequestException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.model.ArticleDTO;
import com.bootcamp.desafioSpring.model.ParamsDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.repository.IMarketPlaceRepository;
import com.bootcamp.desafioSpring.repository.MarketPlaceRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MarketPlaceRepositoryTest {

    IMarketPlaceRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new MarketPlaceRepositoryImpl();
    }

    @Test
    public void getAll() throws OrderException {
        List<ProductDTO> products = MarketPlaceRepositoryImpl.getFromJSON("products.json");

        Assertions.assertEquals(products.size(), repository.getProducts(new ParamsDTO()).size());
        Assertions.assertIterableEquals(products, repository.getProducts(new ParamsDTO()));
    }

    @Test
    public void getAllBadPath() {
        List<ProductDTO> actual = MarketPlaceRepositoryImpl.getFromJSON("productOOs.json");

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void getByName() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        a.setProductId(0);
        expected.add(a);

        ParamsDTO param = new ParamsDTO();
        param.setName("Desmalezadora");

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByWrongNameEmptySetExpected() throws OrderException {

        List<ProductDTO> expected = new ArrayList<>();

        ParamsDTO param = new ParamsDTO();
        param.setName("DeZmalezadora");

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(0, actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByCategory() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        ProductDTO b = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO c = new ProductDTO("Soldadora", "Herramientas", "Black & Decker", 7215.0, 10, true, 3);
        a.setProductId(0);
        b.setProductId(1);
        c.setProductId(2);
        expected.add(a);
        expected.add(b);
        expected.add(c);
        ParamsDTO param = new ParamsDTO();
        param.setCategory("Herramientas");

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByBrand() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO b = new ProductDTO("Soldadora", "Herramientas", "Black & Decker", 7215.0, 10, true, 3);
        a.setProductId(1);
        b.setProductId(2);

        expected.add(a);
        expected.add(b);


        ParamsDTO param = new ParamsDTO();
        param.setBrand("Black & Decker");

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByPrice() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        a.setProductId(0);
        expected.add(a);

        ParamsDTO param = new ParamsDTO();
        param.setPrice(9600.0);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByFreeShipping() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        ProductDTO b = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO c = new ProductDTO("Soldadora", "Herramientas", "Black & Decker", 7215.0, 10, true, 3);
        ProductDTO d = new ProductDTO("Zapatillas Deportivas", "Deportes", "Nike", 14000.0, 4, true, 5);
        ProductDTO e = new ProductDTO("Zapatillas Deportivas", "Deportes", "Adidas", 13650.0, 6, true, 5);
        ProductDTO f = new ProductDTO("Redmi Note 9", "Celulares", "Xiaomi", 40000.0, 15, true, 4);
        ProductDTO g = new ProductDTO("Short", "Indumentaria", "Lacoste", 3900.0, 9, true, 3);

        a.setProductId(0);
        b.setProductId(1);
        c.setProductId(2);
        d.setProductId(3);
        e.setProductId(4);
        f.setProductId(6);
        g.setProductId(11);

        expected.add(a);
        expected.add(b);
        expected.add(c);
        expected.add(d);
        expected.add(e);
        expected.add(f);
        expected.add(g);

        ParamsDTO param = new ParamsDTO();
        param.setFreeShiping(true);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByPrestige() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        ProductDTO b = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO c = new ProductDTO("Redmi Note 9", "Celulares", "Xiaomi", 40000.0, 15, true, 4);
        a.setProductId(0);
        b.setProductId(1);
        c.setProductId(6);
        expected.add(a);
        expected.add(b);
        expected.add(c);

        ParamsDTO param = new ParamsDTO();
        param.setPrestige(4);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByPrestigeAndNameAscOrder() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        ProductDTO b = new ProductDTO("Redmi Note 9", "Celulares", "Xiaomi", 40000.0, 15, true, 4);
        ProductDTO c = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);

        a.setProductId(0);
        b.setProductId(6);
        c.setProductId(1);
        expected.add(a);
        expected.add(b);
        expected.add(c);

        ParamsDTO param = new ParamsDTO();
        param.setPrestige(4);
        param.setOrder(0);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByPrestigeAndNameDescOrder() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO b = new ProductDTO("Redmi Note 9", "Celulares", "Xiaomi", 40000.0, 15, true, 4);
        ProductDTO c = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        a.setProductId(1);
        b.setProductId(6);
        c.setProductId(0);
        expected.add(a);
        expected.add(b);
        expected.add(c);

        ParamsDTO param = new ParamsDTO();
        param.setPrestige(4);
        param.setOrder(1);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByPrestigeAndPriceDescOrder() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Redmi Note 9", "Celulares", "Xiaomi", 40000.0, 15, true, 4);
        ProductDTO b = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO c = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        a.setProductId(6);
        b.setProductId(1);
        c.setProductId(0);
        expected.add(a);
        expected.add(b);
        expected.add(c);

        ParamsDTO param = new ParamsDTO();
        param.setPrestige(4);
        param.setOrder(2);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void getByPrestigeAndPriceAscOrder() throws OrderException {
        List<ProductDTO> expected = new ArrayList<>();
        ProductDTO a = new ProductDTO("Desmalezadora", "Herramientas", "Makita", 9600.0, 5, true, 4);
        ProductDTO b = new ProductDTO("Taladro", "Herramientas", "Black & Decker", 12500.0, 7, true, 4);
        ProductDTO c = new ProductDTO("Redmi Note 9", "Celulares", "Xiaomi", 40000.0, 15, true, 4);
        a.setProductId(0);
        b.setProductId(1);
        c.setProductId(6);
        expected.add(a);
        expected.add(b);
        expected.add(c);

        ParamsDTO param = new ParamsDTO();
        param.setPrestige(4);
        param.setOrder(3);

        List<ProductDTO> actual = repository.getProducts(param);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    public void wrongOrderNumberThrowException() {
        ParamsDTO params = new ParamsDTO();
        params.setOrder(4);

        Assertions.assertThrows(OrderException.class, () -> repository.getProducts(params));

        params.setOrder(-1);

        Assertions.assertThrows(OrderException.class, () -> repository.getProducts(params));

        params.setOrder(0);

        Assertions.assertDoesNotThrow(() -> repository.getProducts(params));

    }

    @Test
    public void saveCorrectPurchaseRequest() throws BadRequestException {
        PurchaseRequestDTO expected = new PurchaseRequestDTO();
        expected.setRequestId(0);
        List<ArticleDTO> articles = new ArrayList<>();
        ArticleDTO a1 = new ArticleDTO();
        ArticleDTO a2 = new ArticleDTO();
        a1.setProductId(0);
        a1.setQuantity(1);
        a2.setProductId(3);
        a2.setQuantity(2);

        articles.add(a1);
        articles.add(a2);

        expected.setArticles(articles);

        repository.savePurchaseRequest(expected);

        Assertions.assertEquals(expected.getArticles().size(), repository.getPurchaseRequest().get(0).getArticles().size());
        Assertions.assertIterableEquals(expected.getArticles(), repository.getPurchaseRequest().get(0).getArticles());
    }

    @Test
    public void saveNullPurchaseRequest() {
        Assertions.assertThrows(BadRequestException.class, () -> repository.savePurchaseRequest(null));
    }

    @Test
    public void deletePurchaseRequest() throws BadRequestException {
        PurchaseRequestDTO pr = new PurchaseRequestDTO();
        pr.setRequestId(0);

        repository.savePurchaseRequest(pr);

        Assertions.assertEquals(1, repository.getPurchaseRequest().size());

        repository.deletePurchaseRequest(0);

        Assertions.assertEquals(0, repository.getPurchaseRequest().size());
    }

    @Test
    public void deletePurchaseRequestWrongId() throws BadRequestException {
        PurchaseRequestDTO pr = new PurchaseRequestDTO();
        pr.setRequestId(0);

        repository.savePurchaseRequest(pr);

        Assertions.assertEquals(1, repository.getPurchaseRequest().size());

        Assertions.assertThrows(BadRequestException.class, () -> repository.deletePurchaseRequest(1));

        Assertions.assertThrows(BadRequestException.class, () -> repository.deletePurchaseRequest(null));
    }
}