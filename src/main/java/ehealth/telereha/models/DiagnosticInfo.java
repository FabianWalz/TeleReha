package ehealth.telereha.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DiagnosticInfo")
public class DiagnosticInfo {
    @Id
    public String id;

    public String PatientId;
    public String DiagnosticInfo;
    public String GivenBy;
    public String GivenDate;

    public DiagnosticInfo(String PatientId, String DiagnosticInfo, String GivenBy, String GivenDate) {
        this.PatientId = PatientId;
        this.DiagnosticInfo = DiagnosticInfo;
        this.GivenBy = GivenBy;
        this.GivenDate = GivenDate;
    }

    public void update(String DiagnosticInfo, String GivenBy, String GivenDate) {
        this.DiagnosticInfo = DiagnosticInfo;
        this.GivenBy = GivenBy;
        this.GivenDate = GivenDate;
    }

    public void toJsonObject(ObjectNode jsonNode) {
        jsonNode.put("diagnosticID", id);
        jsonNode.put("patientId", PatientId);
        jsonNode.put("diagnosticInfo", DiagnosticInfo);
        jsonNode.put("givenBy", GivenBy);
        jsonNode.put("givenDate", GivenDate);
    }
}
