package ehealth.telereha.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "TreatmentInfo")
public class TreatmentInfo {
    @Id
    public String id;

    public String PatientID;
    public String ExerciseInfo;
    public boolean IsCompleted;
    public String Day;
    public String Hour;


    public TreatmentInfo(String PatientID, String ExerciseInfo, boolean IsCompleted, String Day, String Hour)
    {
        this.PatientID = PatientID;
        this.ExerciseInfo = ExerciseInfo;
        this.IsCompleted = IsCompleted;
        this.Day = Day;
        this.Hour=Hour;
    }

    public void toJsonObject(ObjectMapper mapper, ObjectNode jsonObject)
    {
        jsonObject.put("ExerciseID", id);
        jsonObject.put("PatientID", PatientID);
        jsonObject.put("ExerciseInfo", ExerciseInfo);
        jsonObject.put("IsCompleted", IsCompleted);
        jsonObject.put("Day", Day);
        jsonObject.put("Hour", Hour);



    }

    public void update(String patientID, String exerciseInfo, boolean isCompleted, String days , String hour) {
        this.PatientID = patientID;
        this.ExerciseInfo = exerciseInfo;
        this.IsCompleted = isCompleted;
        this.Day = days;
        this.Hour = hour;
    }

//    public String ImageId;
//    public String Description;
//    public Binary Image;
//    public String TreatmentType;
//
//    public TreatmentInfo(Binary Image, String ImageId, String Description, String TreatmentType) {
//        this.Image = Image;
//        this.Description = Description;
//        this.ImageId = ImageId;
//        this.TreatmentType = TreatmentType;
//    }
}
