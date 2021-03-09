package com.bootcamp.desafioSpring.repository;

import com.bootcamp.desafioSpring.exceptions.ClientAlreadyExistException;
import com.bootcamp.desafioSpring.model.ClientDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepository implements IClientRepository {

    List<ClientDTO> clients;
    Integer cont;

    public ClientRepository() {
        this.clients = new ArrayList<>();
        cont = 0;
    }


    @Override
    public void saveClient(ClientDTO client) throws ClientAlreadyExistException {
        for (ClientDTO c : clients) {
            if (c.getDni().equals(client.getDni())) {
                throw new ClientAlreadyExistException();
            }
        }
        client.setClientId(cont);
        this.clients.add(client);
        cont++;
    }

    @Override
    public void deleteClient(Integer id) {
        this.clients.removeIf(c -> c.getClientId().equals(id));
    }

    @Override
    public List<ClientDTO> getClients() {
        return this.clients;
    }
}
