package ehealth.telereha.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ehealth.telereha.constants.LoginConstants;
import ehealth.telereha.models.UserInfo;
import ehealth.telereha.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/listTherapist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTherapist(@RequestParam(required = false, name = "therapistType") String therapistType) {
        return getTherapistResponseEntity(therapistType);
    }

    @GetMapping(value = "/therapistInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> gettherapistInfo(@RequestParam(required = false, name = "therapistId") String therapistId) {
        return getTherapistResponseEntityByID(therapistId);
    }

    private ResponseEntity<Object> getTherapistResponseEntity(String therapistType) {
        Map<String, String> statusCode;

        try {
            List<UserInfo> Therapist = repository.findNamedParameters(therapistType, "Physiotherapist");
            if (Therapist.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE,
                        LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);

                return ResponseEntity.status(HttpStatus.OK).body(statusCode);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();

                ArrayNode therapistNodes = mapper.createArrayNode();
                for (UserInfo user : Therapist) {
                    user.toJsonObject(therapistNodes.addObject());
                }
                rootNode.putArray("Therapist").addAll(therapistNodes);

                return new ResponseEntity<Object>(rootNode, HttpStatus.OK);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getTherapistResponseEntityByID(String therapistId) {
        Map<String, String> statusCode;

        try {
            List<UserInfo> Therapist = repository.findNamedParametersById(therapistId, "Physiotherapist");
            if (Therapist.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);

                return ResponseEntity.status(HttpStatus.OK).body(statusCode);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();

                ArrayNode therapistNodes = mapper.createArrayNode();
                for (UserInfo user : Therapist) {
                    user.toJsonObject(therapistNodes.addObject());
                }
                rootNode.putArray("Therapist").addAll(therapistNodes);

                return new ResponseEntity<Object>(rootNode, HttpStatus.OK);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }
}
