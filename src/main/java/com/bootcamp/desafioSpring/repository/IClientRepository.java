package com.bootcamp.desafioSpring.repository;

import com.bootcamp.desafioSpring.exceptions.ClientAlreadyExistException;
import com.bootcamp.desafioSpring.model.ClientDTO;

import java.util.List;

public interface IClientRepository {
    public void saveClient(ClientDTO client) throws ClientAlreadyExistException;

    public void deleteClient(Integer id);

    public List<ClientDTO> getClients();
}
