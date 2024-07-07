package ehealth.telereha.repositories;

import ehealth.telereha.models.DiagnosticInfo;
import ehealth.telereha.models.TreatmentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiagnosticRepository extends MongoRepository<DiagnosticInfo, String> {

    @Query("{'id' : :#{#id}}")
    List<DiagnosticInfo> findNamedParametersByID(@Param("id") String id);

    @Query("{'PatientId' : :#{#PatientId}}")
    List<DiagnosticInfo> findNamedParameters(@Param("PatientId") String PatientId);
}
