package fa.training.fjb04.ims.service.common;

import fa.training.fjb04.ims.entity.Skills;

import java.util.List;

public interface SkillService {
    List<Skills> findAll();

    Skills findByName (String skill);

    List<String> findAllByCandidateId(Integer id);

    List<String> findAllSkill();
}
