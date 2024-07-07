package ehealth.telereha.models;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class AppointmentDetailInfo {
    public String AppointmentType;
    public String ConfirmRequestType;
    public String ProblemDescription;
    public String RecipeInfoId;

    public AppointmentDetailInfo(String AppointmentType, String ConfirmRequestType, String ProblemDescription, String RecipeInfoId) {

        this.AppointmentType = AppointmentType;
        this.ConfirmRequestType = ConfirmRequestType;
        this.ProblemDescription = ProblemDescription;
        this.RecipeInfoId = RecipeInfoId;
    }

    public void toJsonObject(ObjectNode jsonNode) {
        jsonNode.put("appointmentType", this.AppointmentType);
        jsonNode.put("confirmRequestType", this.ConfirmRequestType);
        jsonNode.put("problemDescription", this.ProblemDescription);
        jsonNode.put("recipeInfoId", this.RecipeInfoId);
    }
}
