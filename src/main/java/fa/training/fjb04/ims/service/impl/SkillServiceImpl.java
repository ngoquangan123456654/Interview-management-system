package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.entity.Skills;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.service.common.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public List<Skills> findAll() {
        return skillRepository.findAll();
    }

    @Override
    public Skills findByName(String skill) {
        return skillRepository.findByName(skill);
    }

    @Override
    public List<String> findAllByCandidateId(Integer id) {
        return skillRepository.findAllByCandidateId(id);
    }

    @Override
    public List<String> findAllSkill() {
        return skillRepository.findAllSkill();
    }
}
