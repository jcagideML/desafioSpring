package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.ClientAlreadyExistException;
import com.bootcamp.desafioSpring.exceptions.MissingDataException;
import com.bootcamp.desafioSpring.model.ClientDTO;
import com.bootcamp.desafioSpring.model.DomicilioDTO;
import com.bootcamp.desafioSpring.repository.IClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("clientService")
public class ClientService implements IClientService {

    @Autowired
    private final IClientRepository repository;

    public ClientService(IClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void newClient(ClientDTO client) throws ClientAlreadyExistException, MissingDataException {
        if (client.getNombre() != null & client.getApellido() != null & client.getEmail() != null
                & checkDomicilio(client.getDomicilio())) {
            this.repository.saveClient(client);
        } else {
            throw new MissingDataException();
        }
    }

    @Override
    public List<ClientDTO> getClients() {
        return this.repository.getClients();
    }

    @Override
    public void deleteClient(Integer id) {
        this.repository.deleteClient(id);
    }

    @Override
    public List<ClientDTO> getClientByProvincia(String provincia) {
        List<ClientDTO> result = new ArrayList<>();
        for (ClientDTO c : this.repository.getClients()) {
            if (c.getDomicilio().getProvincia().equals(provincia)) {
                result.add(c);
            }
        }
        return result;
    }

    private boolean checkDomicilio(DomicilioDTO domicilio) {
        return domicilio.getAltura() != null & domicilio.getCalle() != null & domicilio.getLocalidad() != null
                & domicilio.getProvincia() != null & domicilio.getCodPostal() != null;
    }
}