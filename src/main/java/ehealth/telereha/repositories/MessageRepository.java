package ehealth.telereha.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import ehealth.telereha.models.MsgInfo;

public interface MessageRepository extends MongoRepository<MsgInfo, String> {

    @Aggregation(pipeline = {
            "{ '$match': { '$or': [ { '$and': [ { 'From' : ?0 }, { 'To' : ?1 } ] }, { '$and': [ { 'To' : ?0 }, { 'From' : ?1 } ] } ] } }",
            "{ '$sort' : { 'TimeInMillis' : -1 } }",
            "{ '$limit' : ?2 }"
    })
    List<MsgInfo> findNamedParameters(String from, String to, int limit);
}
