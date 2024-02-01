package fa.training.fjb04.ims.repository.job;

import fa.training.fjb04.ims.entity.intermediateTable.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM JobSkill js WHERE js.job.id = :id")
    void deleteAllById(@Param("id") Integer id);

}
