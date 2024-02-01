package fa.training.fjb04.ims.repository.candidate;

import fa.training.fjb04.ims.entity.intermediateTable.CandidateJob;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateJobRepository extends JpaRepository<CandidateJob, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CandidateJob cj WHERE cj.candidate.candidateId = :id")
    void deleteAllByCandidate(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM CandidateJob jb WHERE jb.job.id = :id")
    void deleteAllById(@Param("id") Integer id);
}
