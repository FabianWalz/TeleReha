package ehealth.telereha.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ehealth.telereha.models.UserInfo;
import ehealth.telereha.repositories.UserRepository;
import ehealth.telereha.utils.HashValueGenerator;
import ehealth.telereha.utils.RandomValueGenerator;

@RestController
@RequestMapping("/reset-password")
public class PasswordResetController {

    @Autowired
    private final UserRepository userRepository;

    public PasswordResetController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateResetCode(@RequestParam("email") String email) {
        Optional<UserInfo> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();

            // Generiert neues Passwort
            String newPassword = RandomValueGenerator.getRandomPassword();
            try {
                Pair<String, String> hashedValue = HashValueGenerator.calculateHashedValue(newPassword);
                user.setPw(hashedValue.getFirst());
                user.setSalt(hashedValue.getSecond());
                userRepository.save(user);

                return ResponseEntity.ok(Map.of("success", true, "newPassword", newPassword));
            } catch (NoSuchAlgorithmException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "Error generating new password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "User not found"));
        }
    }
}
