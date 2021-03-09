package com.bootcamp.desafioSpring.repository;

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
        products = getFromJSON(); //Guardo lo que levanto del archivo en memoria, para no llamarlo reiteradas veces.
        purchaseRequests = new ArrayList<>();
        sells = new ArrayList<>();
        prCont = 0;
        sCont = 0;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return products;
    }

    @Override
    public void savePurchaseRequest(PurchaseRequestDTO purchaseRequest) {
        purchaseRequest.setRequestId(prCont);
        this.purchaseRequests.add(purchaseRequest);
        prCont++;
    }

    @Override
    public void deletePurchaseRequest(Integer id) {
        this.purchaseRequests.removeIf(p -> p.getRequestId().equals(id));
    }

    @Override
    public List<PurchaseRequestDTO> getPurchaseRequest() {
        return this.purchaseRequests;
    }

    @Override
    public void saveSell(PurchaseRequestResponseDTO purchaseRequestResponse) {
        purchaseRequestResponse.getTicket().setTicketId(sCont);
        this.sells.add(purchaseRequestResponse);
        sCont++;
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
    private List<ProductDTO> getFromJSON() {
        List<ProductDTO> products = new ArrayList<>();
        FileReader reader;
        File file;
        try {
            file = ResourceUtils.getFile("classpath:products.json");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /*
    Metodo auxiliar para quitar los simbolos de $ y puntos de la columna "Precio".
     */
    private String cleanPrice(String price) {
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
