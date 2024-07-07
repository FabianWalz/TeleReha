package ehealth.telereha.repositories;

import ehealth.telereha.models.AppointmentInfo;
import ehealth.telereha.models.TreatmentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TreatmentRepository extends MongoRepository<TreatmentInfo, String> {

    @Query("{'PatientID' : :#{#PatientID}}")
    List<TreatmentInfo> findNamedParameters(@Param("PatientID") String PatientID);

    @Query("{'ImageId' : :#{#ImageId}}")
    List<TreatmentInfo> findImage(@Param("ImageId") String ImageId);

    @Query("{'id' : :#{#id}}")
    List<TreatmentInfo> findNamedParametersByID(@Param("id") String id);
}
