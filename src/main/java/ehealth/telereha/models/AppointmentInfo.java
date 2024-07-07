package ehealth.telereha.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "AppointmentInfo")
public class AppointmentInfo {
    @Id
    public String id;

    public String TherapistID;

    public String PatientId;

    public String TherapistType;

    public String Date;

    public String Hour;
    // Minutes
    public Integer AppointmentDuration;

    public String Location;

    public AppointmentDetailInfo Detail;

    public AppointmentInfo(String TherapistID, String TherapistType, String PatientId, String Date, String Hour,
                           Integer AppointmentDuration ,String Location, AppointmentDetailInfo Detail ) {
        this.TherapistID = TherapistID;
        this.TherapistType = TherapistType;
        this.PatientId = PatientId;
        this.Date = Date;
        this.Hour = Hour;
        this.AppointmentDuration = AppointmentDuration;
        this.Location=Location;
        this.Detail = Detail;
       
      
    }

//    public void addNewAppointment(String patientId, String date, AppointmentDetailInfo detail) {
//        Dates.add(date);
//        PatientIds.add(patientId);
//        AppointmentDurations.add(60);
//        Details.add(detail);
//    }

    public void update(String therapistID, String therapistType, String patientId, String appointmentType,
                       String confirmRequestType, String problemDescription, String date, String hour, String recipeInfoId, String Location) {
        this.TherapistID = therapistID;
        this.TherapistType = therapistType;
//        for (int i = 0; i < this.PatientIds.size(); i++) {
//            if (this.PatientIds.get(i).equals(patientId)) {
//                this.PatientIds.set(i, patientId);
//                this.AppointmentDurations.set(i, 60);
//                this.Dates.set(i, date);
//                this.Details.set(i, new AppointmentDetailInfo(appointmentType, confirmRequestType, problemDescription, recipeInfoId));
//                break;
//            }
//        }

        this.PatientId = patientId;
        this.AppointmentDuration = 60;
        this.Date = date;
        this.Hour=hour;
        this.Location=Location;
        this.Detail = new AppointmentDetailInfo(appointmentType, confirmRequestType, problemDescription, recipeInfoId);
    }

    public void toJsonObject(ObjectNode jsonNode) {
        jsonNode.put("appointmentID", id);
        jsonNode.put("therapistID", TherapistID);
        jsonNode.put("therapistType", TherapistType);
        jsonNode.put("date", Date);
        jsonNode.put("hour",Hour);
        jsonNode.put("patientID", PatientId);
        jsonNode.put("appointmentDuration", AppointmentDuration);
        jsonNode.put("location", Location);
        Detail.toJsonObject(jsonNode);
    }
}
