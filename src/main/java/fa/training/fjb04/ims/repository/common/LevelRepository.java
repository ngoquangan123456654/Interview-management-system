package fa.training.fjb04.ims.repository.common;

import fa.training.fjb04.ims.entity.common.Level;
import fa.training.fjb04.ims.entity.intermediateTable.JobLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {

    Level findByName (String name);

    @Query(
            "SELECT jl FROM Level l " +
                    "JOIN JobLevel jl ON l.id = jl.level.id " +
                    "JOIN Job j ON j.id = jl.job.id " +
                    "WHERE j.id = :id"
    )
    List<JobLevel> getAllByJobId(Integer id);

    @Query(
            "SELECT l.name FROM Level l " +
                    "JOIN JobLevel jl ON l.id = jl.level.id " +
                    "JOIN Job j ON j.id = jl.job.id " +
                    "WHERE j.id = :id"
    )
    List<String> getAllLevelNameByJobId(Integer id);

    @Query(value = "select l.name from Level l")
    List<String> findAllLevels();

}
