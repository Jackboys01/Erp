package com.ERP.controllers;

import com.ERP.entities.Client;
import com.ERP.entities.Project;
import com.ERP.exceptions.ClientNotFoundException;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.services.ClientService;
import com.ERP.services.ProjectService;
import com.ERP.utils.MyResponseGenerator;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProjectService projectService;

//    @PostMapping("/add")
//    public ResponseEntity<Client>addClient(@RequestBody Client client)
//    {
//        return  ResponseEntity.ok(clientService.addClient(client));
//    }

    @PostMapping("/add")
    public ResponseEntity<Object> addClient (@Valid @RequestBody Client client) {
        try {
            Client newClient = clientService.addClient(client);
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "Client is added", newClient);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error adding client: " + e.getMessage(), null);
        }
    }


    @PutMapping("/update/{clientId}")
    public ResponseEntity<Object> updateClient(@Valid @RequestBody Client client, @PathVariable Long clientId) {
        try {
            Client updatedClient = clientService.updateClient(client, clientId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Client is updated successfully", updatedClient);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error updating client: " + e.getMessage(), null);
        }
    }

    @GetMapping("/find/{clientId}")
    public ResponseEntity<Object> findClient(@PathVariable long clientId) {
        try {
            Client clientToFind = clientService.findClient(clientId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Client is found", clientToFind);
        } catch (IdNotFoundException e) {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Client not found with id: " + clientId, null);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error finding client: " + e.getMessage(), null);
        }
    }
    @GetMapping("/findAll")
    @ApiOperation(value = "it is  to fetch list of all the clients")

    public ResponseEntity<Object> findAllClients() {
        try {
            List<Client> clients = clientService.findAllClients();
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Clients are found", clients);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error finding clients: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity<Object> deleteClient(@PathVariable long clientId) {
        try {
            Client deletedClient = clientService.deleteClient(clientId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Client is deleted successfully", deletedClient);
        } catch (IdNotFoundException e) {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Client not found with id: " + clientId, null);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error deleting client: " + e.getMessage(), null);
        }
    }

    @PostMapping("/{clientId}/projects/add")
    public ResponseEntity<Object> addProjectToClient(@PathVariable Long clientId, @Valid @RequestBody Project project) {
        try {
            Project newProject = projectService.addProjectToClient(clientId, project);
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "Project is added to client", newProject);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error adding project: " + e.getMessage(), null);
        }
    }

    @GetMapping("/{clientId}/projects")
    public ResponseEntity<Object> getClientProjects(@PathVariable Long clientId) {
        try {
            List<Project> projects = projectService.getProjectsByClient(clientId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Projects are found", projects);
        } catch (IdNotFoundException e) {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Client not found with id: " + clientId, null);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error finding projects: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{clientId}/projects/{projectId}/update-status")
    public ResponseEntity<Object> updateProjectStatus(@PathVariable Long clientId, @PathVariable Long projectId, @RequestParam String status) {
        try {
            Project updatedProject = projectService.updateProjectStatus(clientId, projectId, status);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Project status is updated", updatedProject);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error updating project status: " + e.getMessage(), null);
        }
    }
}
