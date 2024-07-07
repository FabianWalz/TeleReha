package ehealth.telereha.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ehealth.telereha.constants.LoginConstants;
import ehealth.telereha.models.TreatmentInfo;
import ehealth.telereha.repositories.TreatmentRepository;
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
public class TreatmentController {
    @Autowired
    private final TreatmentRepository repository;

    public TreatmentController(TreatmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/listExercises", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListExercises(@RequestParam(required = true, name = "patientID") String patientID) {
        return getExercisesInfoEntity(patientID);
    }

    @GetMapping(value = "/updateExercise", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUpdateExercise(@RequestParam(required = true, name = "exerciseId") String exerciseId,
                                                    @RequestParam(required = true, name = "patientID") String patientID,
                                                    @RequestParam(required = true, name = "exerciseInfo") String exerciseInfo,
                                                    @RequestParam(required = true, name = "isCompleted") boolean isCompleted,
                                                    @RequestParam(required = true, name = "Days") String days,
                                                    @RequestParam(required = true, name = "Hour") String hour
     ) {
        return getUpdateExerciseInfoEntity(exerciseId, patientID, exerciseInfo, isCompleted, days,hour);
    }

    @GetMapping(value = "/addExercise", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAddExercise(@RequestParam(required = true, name = "patientID") String patientID,
                                                 @RequestParam(required = true, name = "exerciseInfo") String exerciseInfo,
                                                 @RequestParam(required = true, name = "Days") String days,
                                                 @RequestParam(required = true, name = "Hour") String hour
                                        ) {
        return getAddExerciseInfoEntity(patientID, exerciseInfo, days,hour);
    }

    @GetMapping(value = "/deleteExercise", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDeleteExercise(@RequestParam(required = true, name = "exerciseId") String exerciseId) {
        return getDeleteExerciseInfoEntity(exerciseId);
    }

    private ResponseEntity<Object> getDeleteExerciseInfoEntity(String exerciseId) {
        Map<String, String> statusCode;

        List<TreatmentInfo> treatmentInfos = repository.findNamedParametersByID(exerciseId);
        if (treatmentInfos.isEmpty()) {
            statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                    LoginConstants.MESSAGE, LoginConstants.APPOINTMENT_ID_NOTFOUND);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
        } else {
            repository.delete(treatmentInfos.get(0));
            statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE,
                    LoginConstants.MESSAGE, "");
            return ResponseEntity.status(HttpStatus.OK).body(statusCode);
        }
    }

    private ResponseEntity<Object> getAddExerciseInfoEntity(String patientID, String exerciseInfo, String days, String hour) {
        Map<String, String> statusCode;

        try {
            TreatmentInfo info = repository.insert(new TreatmentInfo(patientID, exerciseInfo, false, days,hour));
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ObjectNode jsonObj = mapper.createObjectNode();
            info.toJsonObject(mapper, jsonObj);
            rootNode.set("exercise", jsonObj);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getUpdateExerciseInfoEntity(String exerciseId, String patientID, String exerciseInfo,
                                                               boolean isCompleted, String days, String hour) {
        Map<String, String> statusCode;

        try {
            List<TreatmentInfo> infos = repository.findNamedParametersByID(exerciseId);
            if (infos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.EXERCISE_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            } else {
                TreatmentInfo info = infos.get(0);
                info.update(patientID, exerciseInfo, isCompleted, days,hour);

                info = repository.save(info);
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();
                ObjectNode jsonObj = mapper.createObjectNode();
                info.toJsonObject(mapper, jsonObj);
                rootNode.set("exercise", jsonObj);

                return new ResponseEntity<Object>(rootNode, HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

    private ResponseEntity<Object> getExercisesInfoEntity(String patientID) {
        Map<String, String> statusCode;
        try {
            List<TreatmentInfo> infos = repository.findNamedParameters(patientID);
            if (infos.isEmpty()) {
                statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                        LoginConstants.MESSAGE, LoginConstants.USER_ID_NOTFOUND);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode arrayNodes = mapper.createArrayNode();
            for (TreatmentInfo info : infos) {
                info.toJsonObject(mapper, arrayNodes.addObject());
            }
            rootNode.putArray("exercises").addAll(arrayNodes);

            return new ResponseEntity<Object>(rootNode, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusCode);
    }

//    @GetMapping(value = "/treatment", produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] getTreatmentImageWithParams(@RequestParam(required = true, name = "imageId") String imageId) {
//        return getTreatmentImageEntity(imageId);
//    }

//    private byte[] getTreatmentImageEntity(String imageId) {
//        List<TreatmentInfo> parameters = repository.findImage(imageId);
//        if (parameters.size() > 0) {
//            return parameters.get(0).Image.getData();
//        }
//
//        return new byte[0];
//    }

    private ResponseEntity<Object> getTreatmentInfoEntity(String type) {
        return null;
    }
}
