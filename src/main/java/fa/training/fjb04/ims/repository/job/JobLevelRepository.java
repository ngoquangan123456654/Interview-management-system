package fa.training.fjb04.ims.repository.job;

import fa.training.fjb04.ims.entity.intermediateTable.JobLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobLevelRepository extends JpaRepository<JobLevel, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM JobLevel jl WHERE jl.job.id = :id")
    void deleteAllById(@Param("id") Integer id);
}
