package ehealth.telereha.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MsgInfo")
public class MsgInfo {
    @Id
    public String id;
    public String From;
    public String To;
    public String Msg;
    public long TimeInMillis;

    public MsgInfo(String From, String To, String Msg, long TimeInMillis) {
        this.From = From;
        this.To = To;
        this.Msg = Msg;
        this.TimeInMillis = TimeInMillis;
    }

    public void toJson(ObjectNode jsonNode) {
        jsonNode.put("from", this.From);
        jsonNode.put("to", this.To);
        jsonNode.put("msg", this.Msg);
        jsonNode.put("time", this.TimeInMillis);
    }
}
