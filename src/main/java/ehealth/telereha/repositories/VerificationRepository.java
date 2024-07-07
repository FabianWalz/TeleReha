package ehealth.telereha.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ehealth.telereha.models.VerificationCode;

public interface VerificationRepository extends MongoRepository<VerificationCode, String> {
    Optional<VerificationCode> findByEmail(String email);
    void deleteByEmail(String email); 
}
