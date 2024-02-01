package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.dto.job.JobDto;
import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.entity.Skills;
import fa.training.fjb04.ims.entity.common.Benefit;
import fa.training.fjb04.ims.entity.common.Level;
import fa.training.fjb04.ims.entity.intermediateTable.JobBenefit;
import fa.training.fjb04.ims.entity.intermediateTable.JobLevel;
import fa.training.fjb04.ims.entity.intermediateTable.JobSkill;
import fa.training.fjb04.ims.repository.common.BenefitRepository;
import fa.training.fjb04.ims.repository.common.LevelRepository;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.repository.job.JobBenefitRepository;
import fa.training.fjb04.ims.repository.job.JobLevelRepository;
import fa.training.fjb04.ims.repository.job.JobRepository;
import fa.training.fjb04.ims.repository.job.JobSkillRepository;
import fa.training.fjb04.ims.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import fa.training.fjb04.ims.entity.common.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final BenefitRepository benefitRepository;
    private final LevelRepository levelRepository;
    private final JobSkillRepository jobSkillRepository;
    private final JobLevelRepository jobLevelRepository;
    private final JobBenefitRepository jobBenefitRepository;

    @Override
    public Job findByTitle(String title) {
        return jobRepository.findByTitle(title);
    }

    @Override
    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    @Override
    public List<String> getAllTitle() {
        return jobRepository.findAllJobTitle();
    }

    @Override
    public List<String> getAllTitleByStatus(String status) {
        return jobRepository.findAllJobTitleByStatus(status);
    }

    @Override
    public void create(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Optional<Job> findById(Integer id) {
        return jobRepository.findById(id);
    }

    @Override
    public void update(Integer id) {

    }

    @Override
    public void delete(Integer id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Page<JobDto> searchJobList(String keyword, String status, Pageable pageable) {
        Page<Job> jobPage = jobRepository.getSearchList(keyword, status, pageable);

        return jobPage.map(j -> new JobDto(j.getId(), j.getTitle(),
                j.getStartDate(), j.getEndDate(), skillRepository.getAllSkillNameByJobId(j.getId()),
                levelRepository.getAllLevelNameByJobId(j.getId()), j.getStatus()));
    }

    @Override
    public List<JobDto> showAll() {
        List<Job> jobList = jobRepository.findAll();
        List<JobDto> jobDtoList = new ArrayList<>();

        for (Job job : jobList) {
            JobDto jobDto = JobDto.builder()
                    .jobId(job.getId())
                    .title(job.getTitle())
                    .startDate(job.getStartDate())
                    .endDate(job.getEndDate())
                    .skill(skillRepository.getAllSkillNameByJobId(job.getId()))
                    .level(levelRepository.getAllLevelNameByJobId(job.getId()))
                    .status(job.getStatus())
                    .build();

            jobDtoList.add(jobDto);
        }

        return jobDtoList;
    }


    @Override
    public void save(JobDto jobDto, UserDetails userDetails) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);

        job.setCreatedDate(LocalDateTime.now());
        job.setLastModifiedDate(LocalDateTime.now());
        job.setCreatedBy("Dat Dep Zai");
        job.setLastModifiedBy("Dat Dep Zai");
        job.setCreatedDate(LocalDateTime.now());
        job.setLastModifiedDate(LocalDateTime.now());
        job.setCreatedBy(userDetails.getUsername());
        job.setLastModifiedBy(userDetails.getUsername());
        jobRepository.save(job);
        for (String skillName : jobDto.getSkill()) {
            Skills skills = skillRepository.findByName(skillName);
            JobSkill jobSkill = new JobSkill();
            jobSkill.setJob(job);
            jobSkill.setSkills(skills);
            jobSkillRepository.save(jobSkill);
        }
        for (String benefitName : jobDto.getBenefits()) {
            Benefit benefit = benefitRepository.findByName(benefitName);
            JobBenefit jobBenefit = new JobBenefit();
            jobBenefit.setJob(job);
            jobBenefit.setBenefit(benefit);
            jobBenefitRepository.save(jobBenefit);
        }
        for (String levelName : jobDto.getLevel()) {
            Level level = levelRepository.findByName(levelName);
            JobLevel jobLevel = new JobLevel();
            jobLevel.setJob(job);
            jobLevel.setLevel(level);
            jobLevelRepository.save(jobLevel);
        }

        jobRepository.save(job);
        updateJobStatus();
    }


    @Override
    public JobDto findJobDtoById(Integer id) {
        Optional<Job> oJob = jobRepository.findById(id);
        if (oJob.isPresent()) {
            Job job = oJob.get();

            return JobDto.builder()
                    .jobId(job.getId())
                    .title(job.getTitle())
                    .startDate(job.getStartDate())
                    .endDate(job.getEndDate())
                    .salaryFrom(job.getSalaryFrom())
                    .salaryTo(job.getSalaryTo())
                    .workingAddress(job.getWorkingAddress())
                    .benefits(benefitRepository.getAllNameByJobId(job.getId()))
                    .skill(skillRepository.getAllSkillNameByJobId(job.getId()))
                    .level(levelRepository.getAllLevelNameByJobId(job.getId()))
                    .status(job.getStatus())
                    .description(job.getDescription())
                    .build();
        }

        return null;
    }

    @Override
    public void updateJobById(Integer id, JobDto jobDto) {
        Optional<Job> oJob = jobRepository.findById(id);
        if (oJob.isPresent()) {
            Job job = oJob.get();
            BeanUtils.copyProperties(jobDto, job);

            job.setLastModifiedDate(LocalDateTime.now());
            job.setLastModifiedBy("...");

//            job.setJobBenefitList(benefitRepository.getAllByJobId(jobDto.getJobId()));
//            job.setJobLevelList(levelRepository.getAllByJobId(jobDto.getJobId()));
//            job.setJobSkillList(skillRepository.getAllByJobId(jobDto.getJobId()));

            jobSkillRepository.deleteAllById(id);
            for (String skillName : jobDto.getSkill()) {
                Skills skill = skillRepository.findByName(skillName);
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJob(job);
                jobSkill.setSkills(skill);

                jobSkillRepository.save(jobSkill);
            }

            jobLevelRepository.deleteAllById(id);
            for (String levelName : jobDto.getLevel()) {
                Level level = levelRepository.findByName(levelName);
                JobLevel jobLevel = new JobLevel();
                jobLevel.setJob(job);
                jobLevel.setLevel(level);

                jobLevelRepository.save(jobLevel);
            }

            jobBenefitRepository.deleteAllById(id);
            for (String benefitName : jobDto.getBenefits()) {
                Benefit benefit = benefitRepository.findByName(benefitName);
                JobBenefit jobBenefit = new JobBenefit();
                jobBenefit.setJob(job);
                jobBenefit.setBenefit(benefit);

                jobBenefitRepository.save(jobBenefit);
            }

            jobRepository.save(job);
            updateJobStatus();
        }
    }

    @Override
    public void updateJobStatus() {
        List<Job> jobList = jobRepository.findAllJob();

        for (Job job : jobList) {
            if (job.getStartDate().isAfter(LocalDate.now()) && !job.getStartDate().equals(LocalDate.now())) {
                job.setStatus("Draft");
                jobRepository.save(job);
            }
            if ((job.getStartDate().isBefore(LocalDate.now()) || job.getStartDate().equals(LocalDate.now()))
                    && (job.getEndDate().isAfter(LocalDate.now()) || job.getEndDate().equals(LocalDate.now()))) {
                job.setStatus("Open");
                jobRepository.save(job);
            }
            if (job.getEndDate().isBefore(LocalDate.now()) && !job.getEndDate().equals(LocalDate.now())) {
                job.setStatus("Closed");
                jobRepository.save(job);
            }
        }
    }
}
