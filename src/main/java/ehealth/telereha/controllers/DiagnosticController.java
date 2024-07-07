package ehealth.telereha.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ehealth.telereha.constants.LoginConstants;
import ehealth.telereha.models.DiagnosticInfo;
import ehealth.telereha.models.TreatmentInfo;
import ehealth.telereha.repositories.DiagnosticRepository;
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
public class DiagnosticController {
    @Autowired
    private final DiagnosticRepository repository;

    public DiagnosticController(DiagnosticRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/listDiagnostics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListDiagnostics(@RequestParam(required = true, name = "patientID") String patientID) {
        return getDiagnosticsInfoEntity(patientID);
    }

    @GetMapping(value = "/updateDiagnostic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUpdateDiagnostic(@RequestParam(required = true, name = "diagnosticId") String diagnosticId,
                                                    @RequestParam(required = true, name = "givenBy") String givenBy,
                                                    @RequestParam(required = true, name = "givenDate") String givenDate,
                                                    @RequestParam(required = true, name = "diagnosticInfo") String diagnosticInfo) {
        return getUpdateDiagnosticInfoEntity(diagnosticId, diagnosticInfo, givenBy, givenDate);
    }

    @GetMapping(value = "/addDiagnostic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAddDiagnostic(@RequestParam(required = true, name = "patientID") String patientID,
                                                   @RequestParam(required = true, name = "diagnosticInfo") String diagnosticInfo,
                                                   @RequestParam(required = true, name = "givenBy") String givenBy,
                                                   @RequestParam(required = true, name = "givenDate") String givenDate) {
        return getAddDiagnosticInfoEntity(patientID, diagnosticInfo, givenBy, givenDate);
    }

    @GetMapping(value = "/deleteDiagnostic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDeleteDiagnostic(@RequestParam(required = true, name = "diagnosticId") String diagnosticId) {
        return getDeleteDiagnosticInfoEntity(diagnosticId);
    }

    private ResponseEntity<Object> getDiagnosticsInfoEntity(String patientID) {
        Map<String, String> statusCode;
        try {
            List<DiagnosticInfo> infos = repository.findNamedParameters(patientID);
            if (infos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USER_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode arrayNodes = mapper.createArrayNode();
            for (DiagnosticInfo info : infos) {
                info.toJsonObject(arrayNodes.addObject());
            }
            rootNode.putArray("diagnostics").addAll(arrayNodes);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getUpdateDiagnosticInfoEntity(String diagnosticId, String diagnosticInfo, String givenBy, String givenDate) {
        Map<String, String> statusCode;

        try {
            List<DiagnosticInfo> infos = repository.findNamedParametersByID(diagnosticId);
            if (infos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.DIAGNOSTIC_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            } else {
                DiagnosticInfo info = infos.get(0);
                info.update(diagnosticInfo, givenBy, givenDate);

                info = repository.save(info);
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();
                ObjectNode jsonObj = mapper.createObjectNode();
                info.toJsonObject(jsonObj);
                rootNode.set("diagnostic", jsonObj);

                return new ResponseEntity<Object>(rootNode, HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getAddDiagnosticInfoEntity(String patientID, String diagnosticInfo, String givenBy, String givenDate) {
        Map<String, String> statusCode;

        try {
            DiagnosticInfo info = repository.insert(new DiagnosticInfo(patientID, diagnosticInfo, givenBy, givenDate));
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ObjectNode jsonObj = mapper.createObjectNode();
            info.toJsonObject(jsonObj);
            rootNode.set("diagnostic", jsonObj);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getDeleteDiagnosticInfoEntity(String diagnosticId) {
        Map<String, String> statusCode;

        List<DiagnosticInfo> infos = repository.findNamedParametersByID(diagnosticId);
        if (infos.isEmpty()) {
            statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                    LoginConstants.MESSAGE, LoginConstants.APPOINTMENT_ID_NOTFOUND);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
        } else {
            repository.delete(infos.get(0));
            statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE,
                    LoginConstants.MESSAGE, "");
            return ResponseEntity.status(HttpStatus.OK).body(statusCode);
        }
    }
}
