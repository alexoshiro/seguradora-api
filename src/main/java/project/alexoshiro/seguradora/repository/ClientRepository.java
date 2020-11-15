package project.alexoshiro.seguradora.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import project.alexoshiro.seguradora.model.Client;

public interface ClientRepository extends MongoRepository<Client, String> {

}
