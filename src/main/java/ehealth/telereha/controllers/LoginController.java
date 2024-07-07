package ehealth.telereha.controllers;

import java.util.List;
import java.util.Map;

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
import ehealth.telereha.repositories.LoginRepository;
import ehealth.telereha.utils.HashValueGenerator;

@RestController
public class LoginController {

    @Autowired
    private final LoginRepository repository;

    public LoginController(LoginRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserInfoWithParams(@RequestParam(required = true, name = "email") String email,
                                                        @RequestParam(required = true, name = "pw") String pw) {
        return getUserInfoResponseEntity(email, pw);
    }

    private ResponseEntity<Object> getUserInfoResponseEntity(String email, String pw) {
        Map<String, String> statusCode;
        try {
            List<UserInfo> users = repository.findByEmail(email);
            if (users.isEmpty()) {
                statusCode = Map.of(
                        LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USERNAME_OR_PASSWORD_IS_INCORRECT
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }

            Pair<String, String> hashedValue = HashValueGenerator.calculateHashedValue(pw, users.get(0).getSalt());

            users = repository.findByEmailAndPw(email, hashedValue.getFirst());
            if (users.isEmpty()) {
                statusCode = Map.of(
                        LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USERNAME_OR_PASSWORD_IS_INCORRECT
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }

            UserInfo user = users.get(0);
            statusCode = Map.of(
                    LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE,
                    LoginConstants.MESSAGE, LoginConstants.LOGIN_SUCCESS,
                    LoginConstants.USER_ID, user.getUserID(),
                    LoginConstants.USER_INFO, user.toString(),
                    LoginConstants.USER_FNAME, user.getFname(),
                    LoginConstants.USER_LNAME, user.getLname(),
                    LoginConstants.USER_ROLE, user.getRole()
            );
            return ResponseEntity.status(HttpStatus.OK).body(statusCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(
                LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.LOGIN_FAILED
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }
}
