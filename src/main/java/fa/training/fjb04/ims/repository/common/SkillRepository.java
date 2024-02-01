package fa.training.fjb04.ims.repository.common;

import fa.training.fjb04.ims.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import fa.training.fjb04.ims.entity.intermediateTable.JobSkill;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Integer> {

    Skills findByName(String name);

    @Query(value = "SELECT s.name FROM Skills s")
    List<String> findAllSkill();

    @Query(value = "SELECT c.skill.name FROM CandidateSkill c where c.candidate.candidateId =:id")
    List<String> findAllByCandidateId(@Param("id") Integer candidateId);

    @Query(
            "SELECT js FROM Skills s " +
                    "JOIN JobSkill js ON s.id = js.skills.id " +
                    "JOIN Job j ON j.id = js.job.id " +
                    "WHERE j.id = :id"
    )
    List<JobSkill> getAllByJobId(Integer id);


    @Query(
            "SELECT s.name FROM Skills s " +
                    "JOIN JobSkill js ON s.id = js.skills.id " +
                    "JOIN Job j ON j.id = js.job.id " +
                    "WHERE j.id = :id"
    )
    List<String> getAllSkillNameByJobId(Integer id);
}
