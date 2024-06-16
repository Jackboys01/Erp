package com.ERP.controllers;

import com.ERP.entities.Client;
import com.ERP.exceptions.ClientNotFoundException;
import com.ERP.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GraphqlController {
    @Autowired
    private ClientService clientService;
//    @ResponseBody
//    @QueryMapping("getClientById")
//    public Client findClient(@Argument long clientId) throws ClientNotFoundException {
//        return clientService.findClient(clientId);
//    }

    @ResponseBody
    @QueryMapping("getAllClient")
    public List<Client> findAllClients() throws ClientNotFoundException {
        return clientService.findAllClients();
    }
}
