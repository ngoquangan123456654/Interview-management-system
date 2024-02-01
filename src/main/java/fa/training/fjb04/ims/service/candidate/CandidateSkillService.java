package fa.training.fjb04.ims.service.candidate;

import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.entity.intermediateTable.CandidateSkill;

import java.util.List;

public interface CandidateSkillService {
    void saveAll(List<CandidateSkill> candidateSkillList);

    void deleteByCandidateId(Integer id);
}
