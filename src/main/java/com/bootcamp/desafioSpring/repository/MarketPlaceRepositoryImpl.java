package com.bootcamp.desafioSpring.repository;

import com.bootcamp.desafioSpring.exceptions.BadRequestException;
import com.bootcamp.desafioSpring.exceptions.OrderException;
import com.bootcamp.desafioSpring.model.ParamsDTO;
import com.bootcamp.desafioSpring.model.ProductDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestDTO;
import com.bootcamp.desafioSpring.model.PurchaseRequestResponseDTO;
import com.bootcamp.desafioSpring.utils.XLSXToJSONConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class MarketPlaceRepositoryImpl implements IMarketPlaceRepository {

    List<ProductDTO> products;
    List<PurchaseRequestDTO> purchaseRequests;
    List<PurchaseRequestResponseDTO> sells;
    private int prCont;
    private int sCont;

    public MarketPlaceRepositoryImpl() {
        //Hago uso de la clase utilitaria que lee el archivo excel y lo transforma en JSON.
        XLSXToJSONConverter.toJSON(XLSXToJSONConverter.getRowsFromXLSX("Lista de Productos.xlsx"));
        products = getFromJSON("products.json"); //Guardo lo que levanto del archivo en memoria, para no llamarlo reiteradas veces.
        purchaseRequests = new ArrayList<>();
        sells = new ArrayList<>();
        prCont = 0;
        sCont = 0;
    }

    @Override
    public List<ProductDTO> getProducts(ParamsDTO params) throws OrderException {
        List<ProductDTO> products = new ArrayList<>(this.products);

        if (params.getName() != null) {
            products.removeIf(p -> !p.getName().equals(params.getName()));
        }
        if (params.getCategory() != null) {
            products.removeIf(p -> !p.getCategory().equals(params.getCategory()));
        }
        if (params.getBrand() != null) {
            products.removeIf(p -> !p.getBrand().equals(params.getBrand()));
        }
        if (params.getPrice() != null) {
            products.removeIf(p -> !p.getPrice().equals(params.getPrice()));
        }
        if (params.getFreeShiping() != null) {
            products.removeIf(p -> !p.getFreeShiping().equals(params.getFreeShiping()));
        }
        if (params.getPrestige() != null) {
            products.removeIf(p -> !p.getPrestige().equals(params.getPrestige()));
        }

        if (params.getOrder() != null) {
            products = this.orderProducts(products, params.getOrder());
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
    public void savePurchaseRequest(PurchaseRequestDTO purchaseRequest) throws BadRequestException {
        if (purchaseRequest != null) {
            purchaseRequest.setRequestId(prCont);
            this.purchaseRequests.add(purchaseRequest);
            prCont++;
        } else {
            throw new BadRequestException("La solicitud de compra no se pudo emitir. Revise los datos.");
        }
    }

    @Override
    public void deletePurchaseRequest(Integer id) throws BadRequestException {
        for (PurchaseRequestDTO pr : this.purchaseRequests) {
            if (pr.getRequestId().equals(id)) {
                purchaseRequests.remove(pr);
                return;
            }
        }
        throw new BadRequestException("La solicitud de compra no existe en la base de datos.");
    }

    @Override
    public List<PurchaseRequestDTO> getPurchaseRequest() {
        return this.purchaseRequests;
    }

    @Override
    public void saveSell(PurchaseRequestResponseDTO purchaseRequestResponse) throws BadRequestException {
        if (purchaseRequestResponse != null) {
            purchaseRequestResponse.getTicket().setTicketId(sCont);
            this.sells.add(purchaseRequestResponse);
            sCont++;
        } else {
            throw new BadRequestException("La compra no se pudo concretar. Error en la solicitud de finalizacion.");
        }
    }

    @Override
    public List<PurchaseRequestResponseDTO> getSells() {
        return this.sells;
    }

    /*
    Levanto los productos del JSON que creé con la clase Utilitaria.
    Desde acá hago todos los cambios que hacen falta para que a la hora de
    matchear el DTO con los datos del JSON no ocurran fallas.
    Por ej: quitar los signos de peso, los puntos, etc.
    Si se llegaran a agregar más columnas al EXCEL, solo impactaría acá, y habría
    que detallar como interpretar esas nuevas columnas. El cambio el mímino.
     */
    @SuppressWarnings("unchecked")
    public static List<ProductDTO> getFromJSON(String path) {
        List<ProductDTO> products = new ArrayList<>();
        FileReader reader;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (JSONObject jo : (Iterable<JSONObject>) jsonArray) {
                ProductDTO p = new ProductDTO();
                p.setProductId(products.size());
                p.setName((String) jo.get("Nombre"));
                p.setBrand((String) jo.get("Marca"));
                String cantidad = (String) jo.get("Cantidad");
                p.setQuantity(Integer.parseInt(cantidad.substring(0, cantidad.length() - 2)));
                p.setPrestige(jo.get("Prestigio").toString().length());
                p.setCategory((String) jo.get("Categoría"));
                p.setFreeShiping(jo.get("Envío Gratis").equals("SI"));
                p.setPrice(Double.valueOf(cleanPrice((String) jo.get("Precio"))));

                products.add(p);
            }
            reader.close();
            return products;

        } catch (Exception e) {
            return products;
        }
    }

    /*
    Metodo auxiliar para quitar los simbolos de $ y puntos de la columna "Precio".
     */
    private static String cleanPrice(String price) {
        char[] arr = price.toCharArray();
        StringBuilder result = new StringBuilder();

        for (char c : arr) {
            if (c != '$' & c != '.') {
                result.append(c);
            }
        }
        return result.toString();
    }
}
