package ehealth.telereha.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ehealth.telereha.constants.LoginConstants;
import ehealth.telereha.models.UserInfo;
import ehealth.telereha.repositories.RegisterRepository;
import ehealth.telereha.utils.HashValueGenerator;

@RestController
public class RegisterController {
    Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private final RegisterRepository repository;

    public RegisterController(RegisterRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserInfoWithParams(
            @RequestParam(required = true, name = "email") String email,
            @RequestParam(required = true, name = "pw") String pw,
            @RequestParam(required = true, name = "pw2") String pw2,
            @RequestParam(required = true, name = "fname") String fname,
            @RequestParam(required = true, name = "lname") String lname,
            @RequestParam(required = true, name = "bDate") String bDate,
            @RequestParam(required = true, name = "streetAddr") String streetAddr,
            @RequestParam(required = true, name = "houseNumAddr") String houseNumAddr,
            @RequestParam(required = true, name = "cityAddr") String cityAddr,
            @RequestParam(required = true, name = "postalCodeAddr") String postalCodeAddr,
            @RequestParam(required = true, name = "role") String role,
            @RequestParam(required = true, name = "phoneNum") String phoneNum) {

        logger.debug("Received registration request with data: email={}, pw={}, pw2={}, fname={}, lname={}, bDate={}, streetAddr={}, houseNumAddr={}, cityAddr={}, postalCodeAddr={}, role={}, phoneNum={}",
                email, pw, pw2, fname, lname, bDate, streetAddr, houseNumAddr, cityAddr, postalCodeAddr, role, phoneNum);

        return getUserInfoResponseEntity(email, pw, pw2, fname, lname, bDate, streetAddr, houseNumAddr, cityAddr, postalCodeAddr, role, phoneNum);
    }

    private ResponseEntity<Object> getUserInfoResponseEntity(String email, String pw, String pw2, String fname,
                                                             String lname, String bDate, String streetAddr,
                                                             String houseNumAddr, String cityAddr, String postalCodeAddr,
                                                             String role, String phoneNum) {
        Map<String, String> statusCode;
        try {
            List<UserInfo> users = repository.findByEmail(email);
            if (!users.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USERNAME_IS_TAKEN);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }

            if (pw.equals(pw2)) {
                boolean isPwOk = validatePassword(pw);

                if (!isPwOk) {
                    statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                            LoginConstants.MESSAGE, LoginConstants.PASSWORD_NOT_VALID);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
                }

                if (role.equals("praxis") && !isValidPraxisEmail(email)) {
                    statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                            LoginConstants.MESSAGE, "Ungültige E-Mail-Adresse für die Praxis.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
                }

                try {
                    Pair<String, String> hashedValue = HashValueGenerator.calculateHashedValue(pw);

                    repository.insert(new UserInfo(email, hashedValue.getFirst(), hashedValue.getSecond(),
                            fname, lname, role, bDate, streetAddr, houseNumAddr, cityAddr,
                            postalCodeAddr, UUID.randomUUID().toString(), phoneNum));

                    statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE,
                            LoginConstants.MESSAGE, LoginConstants.REGISTER_SUCCESS,
                            LoginConstants.USER_INFO, repository.findByEmail(email).get(0).toString(),
                            LoginConstants.USER_ROLE, role);
                    return ResponseEntity.status(HttpStatus.OK).body(statusCode);

                } catch (NoSuchAlgorithmException e1) {
                    logger.debug(e1.getMessage());
                }
            } else {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.PASSWORDS_NOT_SAME);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }

        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.REGISTER_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private boolean validatePassword(String pw) {
        String pwPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-_])(?=\\S+$).{8,}$";
        return pw.matches(pwPattern);
    }

    private boolean isValidPraxisEmail(String email) {
        List<String> validEmails = List.of(
                "koenigstr@praxis-kuhn.de",
                "info@praxis-kuhn.de",
                "zuffenhausen@praxis-kuhn.de",
                "mail@kuhn-cakirli.de",
                "hoehenpark@praxis-kuhn.de",
                "marienplatz@praxis-kuhn.de",
                "vaihingen@praxis-kuhn.de"
        );
        return validEmails.contains(email);
    }
}
