package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.dto.candidate.CandidateDto;
import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.entity.intermediateTable.CandidateSkill;
import fa.training.fjb04.ims.entity.Skills;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.Position;
import fa.training.fjb04.ims.enums.Status;
import fa.training.fjb04.ims.repository.candidate.CandidateRepository;
import fa.training.fjb04.ims.repository.candidate.CandidateSkillRepository;
import fa.training.fjb04.ims.repository.common.HighLevelRepository;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.service.candidate.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;

    private final UserRepository userRepository;

    private final HighLevelRepository highLevelRepository;

    private final CandidateSkillRepository candidateSkillRepository;

    private final SkillRepository skillRepository;
    @Override
    public Page<CandidateDto> searchCandidateList(String keyword, String status, Pageable pageable) {
        Status enumStatus = (status != null) ? Status.valueOf(status) : null;
        String search = (keyword != null) ? keyword.toLowerCase() : null;
        return candidateRepository.getSearchList(search, enumStatus, pageable);
    }

    @Override
    public Optional<Candidate> findById(Integer id) {
        return candidateRepository.findById(id);
    }

    @Override
    @Transactional
    public void updateCandidate(CandidateDto candidateDto, UserDetails userDetails) {
        Optional<Candidate> oCandidate = candidateRepository.findById(candidateDto.getCandidateId());
        if (oCandidate.isPresent()) {
            Candidate candidate = oCandidate.get();

            BeanUtils.copyProperties(candidateDto, candidate);

    //        candidate.setUser(userRepository.findByFullNameAndRole(candidateDto.getUser()));
            candidate.setHighLevel(highLevelRepository.findByName(candidateDto.getHighLevel()));
            candidate.setGender(Gender.fromValue(candidateDto.getGender()));
            candidate.setPosition(Position.fromValue(candidateDto.getPosition()));
            candidate.setStatus(Status.fromValue(candidateDto.getStatus()));
            candidate.setLastModifiedBy(userDetails.getUsername());
            candidate.setLastModifiedDate(LocalDateTime.now());

            candidateSkillRepository.deleteAllByCandidate(candidate.getCandidateId());
            for (String skillName : candidateDto.getSkill()) {
                Skills skill = skillRepository.findByName(skillName);
                CandidateSkill candidateSkill = new CandidateSkill();
                candidateSkill.setCandidate(candidate);
                candidateSkill.setSkill(skill);
                candidateSkillRepository.save(candidateSkill);

            }
            candidateRepository.save(candidate);
        }
    }


    @Override
    public List<Candidate> getAll() {
        return this.candidateRepository.findAll();
    }


    @Override
    public void deleteById(Integer id) {
        candidateRepository.deleteById(id);
    }

    @Override
    public void save(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    @Override
    public Boolean existsByPhoneNumber(String phoneNumber) {
        return candidateRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return candidateRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByPhoneNumberAndCandidateId(String phoneNumber, Integer candidateId) {
        return candidateRepository.existsByPhoneNumberAndCandidateId(phoneNumber, candidateId);
    }

    @Override
    public Boolean existsByEmailAndCandidateId(String email, Integer id) {
        return candidateRepository.existsByEmailAndCandidateId(email, id);
    }

    @Override
    public List<Candidate> getAllNoSchedule () {
        return candidateRepository.getAllNoSchedule();
    };

    @Override
    public Candidate findByFullName(String name) {
        return candidateRepository.findByFullName(name);
    }

}
