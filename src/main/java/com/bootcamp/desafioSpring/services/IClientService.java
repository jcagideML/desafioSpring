package com.bootcamp.desafioSpring.services;

import com.bootcamp.desafioSpring.exceptions.ClientAlreadyExistException;
import com.bootcamp.desafioSpring.exceptions.MissingDataException;
import com.bootcamp.desafioSpring.model.ClientDTO;

import java.util.List;

public interface IClientService {

    public void newClient(ClientDTO clientRequest) throws ClientAlreadyExistException, MissingDataException;

    public List<ClientDTO> getClients();

    public void deleteClient(Integer id);

    public List<ClientDTO> getClientByProvincia(String provincia);

}
