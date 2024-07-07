package ehealth.telereha.controllers;

import ehealth.telereha.models.UserInfo;
import ehealth.telereha.repositories.ProfileRepository;
import ehealth.telereha.utils.HashValueGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private final ProfileRepository repository;

    public ProfileController(ProfileRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserProfile(@RequestParam("email") String email) {
        Optional<UserInfo> user = repository.findByEmail(email);

        if (user.isPresent()) {
            UserInfo userInfo = user.get();
            logger.debug("Fetched User Data: {}", userInfo); // Logging user data
            return ResponseEntity.ok(userInfo);
        } else {
            logger.debug("User not found for email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUserProfile(@RequestParam("email") String email,
                                                    @RequestBody UserInfo updatedUser) {
        Optional<UserInfo> userOptional = repository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();

            user.setFname(updatedUser.getFname());
            user.setLname(updatedUser.getLname());
            user.setStreetAddr(updatedUser.getStreetAddr());
            user.setHouseNumAddr(updatedUser.getHouseNumAddr());
            user.setCityAddr(updatedUser.getCityAddr());
            user.setPostalCodeAddr(updatedUser.getPostalCodeAddr());
            user.setPhoneNum(updatedUser.getPhoneNum());
            user.setBDate(updatedUser.getBDate()); 

            logger.debug("Updated User Data before saving: {}", user); 

            if (updatedUser.getPw() != null && !updatedUser.getPw().isEmpty()) {
                try {
                    Pair<String, String> hashedValue = HashValueGenerator.calculateHashedValue(updatedUser.getPw());
                    user.setPw(hashedValue.getFirst());
                    user.setSalt(hashedValue.getSecond());
                } catch (Exception e) {
                    logger.error("Error updating password: {}", e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password");
                }
            }

            repository.save(user);
            logger.debug("User Data after saving: {}", user); 
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
