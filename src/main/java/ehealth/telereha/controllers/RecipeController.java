package ehealth.telereha.controllers;

import ehealth.telereha.constants.LoginConstants;
import ehealth.telereha.models.RecipeInfo;
import ehealth.telereha.repositories.RecipeRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {
    @Autowired
    private final RecipeRepository repository;

    public RecipeController(RecipeRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/uploadRecipe")
    public ResponseEntity<Object> fileUpload(@RequestParam(required = false, name = "email") String email,
                                                @RequestParam("file") MultipartFile file) {

        if (file.getOriginalFilename().endsWith(".pdf")) {
            try {
                List<RecipeInfo> parameters = repository.findFile(email);
                if (parameters.size()>0) {
                    parameters.get(0).RecipeFileContent = new Binary(file.getBytes());
                    parameters.get(0).FileName = file.getOriginalFilename();
                    repository.save(parameters.get(0));
                }
                else {
                    repository.save(new RecipeInfo(new Binary(file.getBytes()), file.getOriginalFilename(), email));

                }
                Map<String, String> statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.SUCCESS_CODE);
                return ResponseEntity.status(HttpStatus.OK).body(statusCode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Map<String, String> statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                    LoginConstants.MESSAGE, LoginConstants.NOT_PDF_FILE);

            return ResponseEntity.status(HttpStatus.OK).body(statusCode);
        }

    }

    @GetMapping(value="/downloadRecipe", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Object> fileDownload(@RequestParam(required = false, name = "userId") String userId) {
        List<RecipeInfo> parameters = repository.findFile(userId);
        if (parameters.size() > 0) {
            byte[] data = parameters.get(0).RecipeFileContent.getData();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);

            ResponseEntity<Object> body = ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION,
                    (ContentDisposition.builder("attachment").filename( parameters.get(0).FileName).build()).toString()).body(inputStreamResource);

            return body;
        }

        Map<String, String> statusCode = Map.of(LoginConstants.STATUS_CODE, LoginConstants.FAIL_CODE,
                LoginConstants.MESSAGE, LoginConstants.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(statusCode);
    }
}
