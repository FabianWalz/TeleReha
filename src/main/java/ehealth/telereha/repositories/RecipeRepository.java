package ehealth.telereha.repositories;

import ehealth.telereha.models.RecipeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends MongoRepository<RecipeInfo, String> {

    @Query("{'Email' : :#{#Email}}")
    List<RecipeInfo> findFile(@Param("Email") String Email);
    List<RecipeInfo> getEmail(@Param("Email") String Email);

}
