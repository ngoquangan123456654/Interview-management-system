package fa.training.fjb04.ims.repository.candidate;

import fa.training.fjb04.ims.entity.intermediateTable.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CandidateSkill cs WHERE cs.candidate.candidateId = :id")
    void deleteAllByCandidate(@Param("id") Integer id);
}
