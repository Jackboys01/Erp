package com.ERP.services;
import com.ERP.entities.Client;
import com.ERP.exceptions.ClientNotFoundException;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {this.clientRepository = clientRepository;
    }

    public Client addClient(Client client) throws ClientNotFoundException {
      try{
          clientRepository.save(client);
          return client;
      }
      catch (Exception e)
      {
          throw new ClientNotFoundException("Error adding the client: " + e.getMessage());
      }
    }

    public Client updateClient(Client client, Long clientId) throws ClientNotFoundException {
     try{
         // Get the client which we need to update
         Client client1 = clientRepository.findById(clientId).get();

         // Update client fields if they are not null or empty in clientDto
         if (Objects.nonNull(client.getName()) && !client.getName().isEmpty()) {
             client1.setName(client.getName());
         }
         if (Objects.nonNull(client.getEmail()) && !client.getEmail().isEmpty()) {
             client1.setEmail(client.getEmail());
         }
         if (Objects.nonNull(client.getPhone()) && !client.getPhone().isEmpty()) {
             client1.setPhone(client.getPhone());
         }
         if (Objects.nonNull(client.getAddress()) && !client.getAddress().isEmpty()) {
             client1.setAddress(client.getAddress());
         }
         clientRepository.save(client1);
         return client;

     } catch (Exception e) {
         throw new ClientNotFoundException("Error Updating Project "+ e.getMessage());
     }

    }

    public Client findClient(long clientId) throws ClientNotFoundException {
        try {
            Client client = clientRepository.findById(clientId).orElseThrow(() -> new IdNotFoundException("Client not found with id: " + clientId));
            return client;
        } catch (IdNotFoundException e) {
            throw new IdNotFoundException("This id does not exist: "+clientId);
        } catch (Exception e) {
            throw new ClientNotFoundException("Error finding client: " + e.getMessage());
        }
    }


    public List<Client> findAllClients() throws ClientNotFoundException {

         try{
             return clientRepository.findAll();
         } catch (IdNotFoundException e) {
             throw e;
         } catch (Exception e) {
             throw new ClientNotFoundException("Error finding all the client: " + e.getMessage());
         }

    }

    public Client deleteClient(long clientId) throws ClientNotFoundException {
       try {
           Client client = clientRepository.findById(clientId).get();
            clientRepository.delete(client);
            return client;
       } catch (Exception e) {
           throw new ClientNotFoundException("Error deleting the client: "+ e.getMessage());
       }
    }
}
