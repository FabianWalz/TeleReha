package ehealth.telereha.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ehealth.telereha.constants.LoginConstants;
import ehealth.telereha.models.AppointmentDetailInfo;
import ehealth.telereha.models.AppointmentInfo;
import ehealth.telereha.models.RecipeInfo;
import ehealth.telereha.repositories.AppointmentRepository;
import ehealth.telereha.repositories.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
public class AppointmentController {

    @Autowired
    private final AppointmentRepository repository;

    @Autowired
    private final RecipeRepository recipeRepository;

    public AppointmentController(AppointmentRepository repository, RecipeRepository recipeRepository) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping(value = "/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAvailableDates(@RequestParam(required = true, name = "therapistID") String therapistID,
                                                    @RequestParam(required = true, name = "therapistType") String therapistType) {
        return getAppointmentInfoResponseEntity(therapistID, therapistType);
    }

    @GetMapping(value = "/setappointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getSetAppoint(@RequestParam(required = true, name = "therapistID") String therapistID,
                                                @RequestParam(required = true, name = "therapistType") String therapistType,
                                                @RequestParam(required = true, name = "patientId") String patientId,
                                                @RequestParam(required = true, name = "appointmentType") String appointmentType,
                                                @RequestParam(required = true, name = "confirmRequestType") String confirmRequestType,
                                                @RequestParam(required = true, name = "problemDescription") String problemDescription,
                                                @RequestParam(required = true, name = "date") String date,
                                                @RequestParam(required = true, name = "hour") String hour,
                                                @RequestParam(required = true, name = "location") String location) {
        return getSetAppointResponseEntity(therapistID, therapistType, patientId, appointmentType, confirmRequestType, problemDescription, date,hour,location);
    }

    @GetMapping(value = "/updateappointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUpdateAppoint(
                                                @RequestParam(required = true, name = "appointmentID") String appointmentID,
                                                @RequestParam(required = true, name = "therapistID") String therapistID,
                                                @RequestParam(required = true, name = "therapistType") String therapistType,
                                                @RequestParam(required = true, name = "patientId") String patientId,
                                                @RequestParam(required = true, name = "appointmentType") String appointmentType,
                                                @RequestParam(required = true, name = "confirmRequestType") String confirmRequestType,
                                                @RequestParam(required = true, name = "problemDescription") String problemDescription,
                                                @RequestParam(required = true, name = "date") String date,
                                                @RequestParam(required = true, name = "hour") String hour,
                                                @RequestParam(required = true, name = "location") String location) {
        return getUpdateAppointResponseEntity(appointmentID, therapistID, therapistType, patientId, appointmentType, confirmRequestType, problemDescription, date,hour, location);
    }

    @GetMapping(value = "/deleteappointment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDeleteAppoint(
            @RequestParam(required = true, name = "appointmentID") String appointmentID) {
        return getDeleteAppointmentResponseEntity(appointmentID);
    }

    @GetMapping(value = "/appointmentForPatient", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAppointForPatient(
            @RequestParam(required = true, name = "patientID") String patientID) {
        return getAppointmentResponseEntity(patientID);
    }


    private ResponseEntity<Object> getDeleteAppointmentResponseEntity(String appointmentID) {
        Map<String, String> statusCode;

        List<AppointmentInfo> appointmentInfos = repository.findNamedParametersByID(appointmentID);
        if (appointmentInfos.isEmpty()) {
            statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                    LoginConstants.MESSAGE, LoginConstants.APPOINTMENT_ID_NOTFOUND);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
        } else {
            repository.delete(appointmentInfos.get(0));
            statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE,
                    LoginConstants.MESSAGE, "");
            return ResponseEntity.status(HttpStatus.OK).body(statusCode);
        }
    }

    private ResponseEntity<Object> getSetAppointResponseEntity(String therapistID, String therapistType, String patientId,
                                                               String appointmentType, String confirmRequestType,
                                                               String problemDescription, String date, String hour, String location) {
        Map<String, String> statusCode;

        try {
            String recipeInfoId = getRecipeInfoId(patientId);
            AppointmentDetailInfo detail = new AppointmentDetailInfo(appointmentType, confirmRequestType, problemDescription, recipeInfoId);
            AppointmentInfo appointmentInfo = repository.insert(new AppointmentInfo(therapistID, therapistType, patientId, date,hour, 60,location, detail ));
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ObjectNode appoint = mapper.createObjectNode();
            appointmentInfo.toJsonObject(appoint);
            rootNode.set("appointment", appoint);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getUpdateAppointResponseEntity( String appointmentID,
                                                                   String therapistID, String therapistType, String patientId,
                                                               String appointmentType, String confirmRequestType,
                                                               String problemDescription, String date, String hour , String location) {
        Map<String, String> statusCode;

        try {
            List<AppointmentInfo> appointmentInfos = repository.findNamedParametersByID(appointmentID);
            if (appointmentInfos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.APPOINTMENT_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            } else {
                AppointmentInfo appointmentInfo = appointmentInfos.get(0);
                String recipeInfoId = getRecipeInfoId(patientId);
                appointmentInfo.update(therapistID, therapistType, patientId, appointmentType,
                        confirmRequestType, problemDescription, date,hour, recipeInfoId, location);

                appointmentInfo = repository.save(appointmentInfo);
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();
                ObjectNode appoint = mapper.createObjectNode();
                appointmentInfo.toJsonObject(appoint);
                rootNode.set("appointment", appoint);

                return new ResponseEntity<Object>(rootNode, HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private String getRecipeInfoId(String patientId) {
        List<RecipeInfo> file = recipeRepository.findFile(patientId);
        String recipeInfoId = "";
        if (file.size()>0) {
             recipeInfoId = file.get(0).id;
        }
        return recipeInfoId;
    }

    private ResponseEntity<Object> getAppointmentInfoResponseEntity(String therapistID, String therapistType) {
        Map<String, String> statusCode;
        try {
            List<AppointmentInfo> appointmentInfos = repository.findNamedParameters(therapistID, therapistType);
            if (appointmentInfos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USER_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode appointmentNodes = mapper.createArrayNode();
            for (AppointmentInfo appointmentInfo : appointmentInfos) {
                appointmentInfo.toJsonObject(appointmentNodes.addObject());
            }
            rootNode.putArray("appointments").addAll(appointmentNodes);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }


    private ResponseEntity<Object> getAppointmentResponseEntity(String patientID) {
        Map<String, String> statusCode;
        try {
            List<AppointmentInfo> appointmentInfos = repository.findByPatientIDNamedParameters(patientID);
            if (appointmentInfos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USER_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode appointmentNodes = mapper.createArrayNode();
            for (AppointmentInfo appointmentInfo : appointmentInfos) {
                appointmentInfo.toJsonObject(appointmentNodes.addObject());
            }
            rootNode.putArray("appointments").addAll(appointmentNodes);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);

    }
}
