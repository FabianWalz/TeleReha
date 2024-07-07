package ehealth.telereha.controllers;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ehealth.telereha.models.VerificationCode;
import ehealth.telereha.repositories.VerificationRepository;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Autowired
    private final VerificationRepository repository;

    @Autowired
    private final MongoTemplate mongoTemplate;

    public VerificationController(VerificationRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateCode(@RequestParam("email") String email) {
        try {
            String code = generateRandomCode();

            // Entfernt alte Codes für denselben Benutzer
            repository.deleteByEmail(email);

            // Neuen Code speichern
            VerificationCode verificationCode = new VerificationCode(email, code);
            repository.save(verificationCode);

            return ResponseEntity.ok(Map.of("success", true, "code", code));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validateCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        try {
            Optional<VerificationCode> optionalVerificationCode = repository.findByEmail(email);
            if (optionalVerificationCode.isPresent() && optionalVerificationCode.get().getCode().equals(code)) {
                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid code"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
