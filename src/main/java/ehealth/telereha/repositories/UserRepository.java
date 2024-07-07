package ehealth.telereha.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import ehealth.telereha.models.UserInfo;

public interface UserRepository extends MongoRepository<UserInfo, String> {

    @Query("{'TherapistType' : :#{#TherapistType}, 'Role' : :#{#Role}}")
    List<UserInfo> findNamedParameters(@Param("TherapistType") String TherapistType, @Param("Role") String role);

    @Query("{'UserID' : :#{#UserID}, 'Role' : :#{#Role}}")
    List<UserInfo> findNamedParametersById(@Param("UserID") String userID, @Param("Role") String role);

    @Query("{'email' : :#{#email}}")
    Optional<UserInfo> findByEmail(@Param("email") String email);

    @Query("{'email' : :#{#email}, 'pw' : :#{#pw}}")
    List<UserInfo> findByEmailAndPw(@Param("email") String email, @Param("pw") String pw);
}

