package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.dto.job.JobDto;
import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.entity.intermediateTable.JobBenefit;
import fa.training.fjb04.ims.entity.intermediateTable.JobLevel;
import fa.training.fjb04.ims.entity.intermediateTable.JobSkill;
import fa.training.fjb04.ims.helper.ExcelHelper;
import fa.training.fjb04.ims.repository.common.BenefitRepository;
import fa.training.fjb04.ims.repository.common.LevelRepository;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.repository.job.JobBenefitRepository;
import fa.training.fjb04.ims.repository.job.JobLevelRepository;
import fa.training.fjb04.ims.repository.job.JobRepository;
import fa.training.fjb04.ims.repository.job.JobSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobSkillRepository jobSkillRepository;
    private final BenefitRepository benefitRepository;
    private final JobBenefitRepository jobBenefitRepository;
    private final LevelRepository levelRepository;
    private final JobLevelRepository jobLevelRepository;

    public void save(MultipartFile file) {
        try {
            List<JobDto> jobDtos = ExcelHelper.excelToJobs(file.getInputStream());
            for (int i = 0; i < jobDtos.size(); i++) {
                Job job = Job.builder()
                        .title(jobDtos.get(i).getTitle())
                        .startDate(jobDtos.get(i).getStartDate())
                        .endDate(jobDtos.get(i).getEndDate())
                        .salaryFrom(jobDtos.get(i).getSalaryFrom())
                        .salaryTo(jobDtos.get(i).getSalaryTo())
                        .workingAddress(jobDtos.get(i).getWorkingAddress())
                        .description(jobDtos.get(i).getDescription())
                        .build();

                jobRepository.save(job);

                List<String> skill = jobDtos.get(i).getSkill();

                for (String skillName : skill) {
                    JobSkill jobSkill = JobSkill.builder()
                            .skills(skillRepository.findByName(skillName))
                            .job(job)
                            .build();

                    jobSkillRepository.save(jobSkill);
                }

                List<String> benefit = jobDtos.get(i).getBenefits();

                for (String benefitName : benefit) {
                    JobBenefit jobBenefit = JobBenefit.builder()
                            .benefit(benefitRepository.findByName(benefitName))
                            .job(job)
                            .build();

                    jobBenefitRepository.save(jobBenefit);
                }

                List<String> level = jobDtos.get(i).getLevel();

                for (String levelName : level) {
                    JobLevel jobLevel = JobLevel.builder()
                            .level(levelRepository.findByName(levelName))
                            .job(job)
                            .build();

                    jobLevelRepository.save(jobLevel);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
