package ehealth.telereha.repositories;

import ehealth.telereha.models.AppointmentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<AppointmentInfo, String> {

    @Query("{'TherapistID' : :#{#TherapistID}, 'TherapistType' : :#{#TherapistType}}")
    List<AppointmentInfo> findNamedParameters(@Param("TherapistID") String TherapistID, @Param("TherapistType") String TherapistType);

    @Query("{'TherapistID' : :#{#TherapistID}}")
    List<AppointmentInfo> findNamedParameters(@Param("TherapistID") String TherapistID);

    @Query("{'id' : :#{#id}}")
    List<AppointmentInfo> findNamedParametersByID(@Param("id") String id);

    @Query("{'PatientId' : :#{#PatientId}}")
    List<AppointmentInfo> findByPatientIDNamedParameters(String PatientId);
}
