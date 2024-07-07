package ehealth.telereha.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import ehealth.telereha.models.UserInfo;

public interface RegisterRepository extends MongoRepository<UserInfo, String> {

    @Query("{'email' : :#{#email}}")
    List<UserInfo> findByEmail(@Param("email") String email);
}
