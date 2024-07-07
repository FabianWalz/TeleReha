package ehealth.telereha.services;

import ehealth.telereha.models.MsgInfo;
import ehealth.telereha.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repository;

    public void save(MsgInfo msgInfo) {
        repository.save(msgInfo);
    }

    public List<MsgInfo> getMessages(String from, String to, int limit) {
        return repository.findNamedParameters(from, to, limit);
    }
}
