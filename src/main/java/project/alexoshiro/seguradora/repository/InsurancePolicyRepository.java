package project.alexoshiro.seguradora.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import project.alexoshiro.seguradora.model.InsurancePolicy;

public interface InsurancePolicyRepository extends MongoRepository<InsurancePolicy, String> {

	Optional<InsurancePolicy> findByNumber(long number);

}
