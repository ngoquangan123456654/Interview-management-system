package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.entity.intermediateTable.CandidateSkill;
import fa.training.fjb04.ims.repository.candidate.CandidateSkillRepository;
import fa.training.fjb04.ims.service.candidate.CandidateSkillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateSkillServiceImpl implements CandidateSkillService {

    private final CandidateSkillRepository candidateSkillRepository;

    @Override
    public void saveAll(List<CandidateSkill> candidateSkillList) {
        candidateSkillRepository.saveAll(candidateSkillList);
    }

    @Override
    public void deleteByCandidateId(Integer id) {
        candidateSkillRepository.deleteAllByCandidate(id);
    }
}
