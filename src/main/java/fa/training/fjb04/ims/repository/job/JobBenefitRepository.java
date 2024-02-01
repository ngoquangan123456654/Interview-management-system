package fa.training.fjb04.ims.repository.job;

import fa.training.fjb04.ims.entity.intermediateTable.JobBenefit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobBenefitRepository extends JpaRepository<JobBenefit, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM JobBenefit jb WHERE jb.job.id = :id")
    void deleteAllById(@Param("id") Integer id);
}
