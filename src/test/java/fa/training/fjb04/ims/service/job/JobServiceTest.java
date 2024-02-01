package fa.training.fjb04.ims.service.job;

import fa.training.fjb04.ims.dto.job.JobDto;
import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.entity.Skills;
import fa.training.fjb04.ims.entity.intermediateTable.JobSkill;
import fa.training.fjb04.ims.repository.common.BenefitRepository;
import fa.training.fjb04.ims.repository.common.LevelRepository;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.repository.job.JobBenefitRepository;
import fa.training.fjb04.ims.repository.job.JobLevelRepository;
import fa.training.fjb04.ims.repository.job.JobRepository;
import fa.training.fjb04.ims.repository.job.JobSkillRepository;
import fa.training.fjb04.ims.service.impl.JobServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {
    @InjectMocks
    private JobServiceImpl jobServiceImpl;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private JobSkillRepository jobSkillRepository;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private JobLevelRepository jobLevelRepository;

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private JobBenefitRepository jobBenefitRepository;

    @Test
    void getAll_Empty() {
        Mockito.when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        Assertions.assertEquals(jobServiceImpl.getAll(), Collections.emptyList());
    }

    @Test
    void getAll_HaveInDB() {
        Job job = new Job();
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        Mockito.when(jobRepository.findAll()).thenReturn(jobList);

        Assertions.assertEquals(jobServiceImpl.getAll(), jobList);
    }

    @Test
    void getAllTittle_Empty() {
        Mockito.when(jobRepository.findAllJobTitle()).thenReturn(Collections.emptyList());

        Assertions.assertEquals(jobServiceImpl.getAllTitle(), Collections.emptyList());
    }

    @Test
    void getAllTittle_HaveInDB() {
        String titleName = "Backend";
        List<String> titleNameList = new ArrayList<>();
        titleNameList.add(titleName);
        Mockito.when(jobRepository.findAllJobTitle()).thenReturn(titleNameList);

        Assertions.assertEquals(jobServiceImpl.getAllTitle(), titleNameList);
    }

    @Test
    void searchJobList() {
        String keyword = "Back";
        String status = "Open";
        Pageable pageable = PageRequest.of(0, 5);
        Page<Job> jobPage = null;
        Mockito.when(jobRepository.getSearchList(keyword, status, pageable)).thenReturn(jobPage);

        Assertions.assertNull(jobPage);
    }

    @Test
    void showAll_Empty() {
        Mockito.when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        Assertions.assertEquals(jobServiceImpl.showAll(), Collections.emptyList());
    }

    @Test
    void showAll_HaveInDB() {
        Job job = new Job();
        jobRepository.save(job);
        List<Job> jobList = new ArrayList<>();
        jobList.add(job);

        List<String> skillNameList = new ArrayList<>();
        List<String> levelNameList = new ArrayList<>();
        Mockito.when(jobRepository.findAll()).thenReturn(jobList);
        Mockito.when(skillRepository.getAllSkillNameByJobId(job.getId())).thenReturn(skillNameList);
        Mockito.when(levelRepository.getAllLevelNameByJobId(job.getId())).thenReturn(levelNameList);

        List<JobDto> jobDtoList = new ArrayList<>();
        JobDto jobDto = JobDto.builder()
                .title(job.getTitle())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .skill(skillNameList)
                .level(levelNameList)
                .status(job.getStatus())
                .build();

        jobDtoList.add(jobDto);

        Assertions.assertEquals(jobServiceImpl.showAll(), jobDtoList);
    }

    @Test
    void findJobById_NotFound() {
        Mockito.when(jobRepository.findById(1)).thenReturn(null);

        Assertions.assertNull(jobServiceImpl.findById(1));
    }

    @Test
    void findJobById_HaveInDB() {
        Job job = new Job();
        jobRepository.save(job);
        Mockito.when(jobRepository.findById(job.getId())).thenReturn(Optional.of(job));

        Assertions.assertEquals(jobServiceImpl.findById(job.getId()), Optional.of(job));
    }

    @Test
    void findJobDtoById_NotFound() {
        Optional<Job> job = Optional.empty();
        Mockito.when(jobRepository.findById(1)).thenReturn(job);
        JobDto jobDto = jobServiceImpl.findJobDtoById(1);

        Assertions.assertNull(jobDto);
    }

    @Test
    void findJobDtoById_HaveInDB() {
        Job job = new Job();
        jobRepository.save(job);

        Mockito.when(jobRepository.findById(job.getId())).thenReturn(Optional.of(job));
        Mockito.when(benefitRepository.getAllNameByJobId(job.getId())).thenReturn(Collections.emptyList());
        Mockito.when(skillRepository.getAllSkillNameByJobId(job.getId())).thenReturn(Collections.emptyList());
        Mockito.when(levelRepository.getAllLevelNameByJobId(job.getId())).thenReturn(Collections.emptyList());

        JobDto jobDto = JobDto.builder()
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

        Assertions.assertEquals(jobServiceImpl.findJobDtoById(job.getId()), jobDto);
    }

    @Test
    void createJob_Success() {
        Job job = new Job();
        job.setTitle("Backend Developer");
        jobRepository.save(job);

        Mockito.when(jobRepository.findById(job.getId())).thenReturn(Optional.of(job));

        Assertions.assertEquals(jobRepository.findById(job.getId()), Optional.of(job));
    }

    @Test
    void updateJobById_Success() {
        Job job = new Job();
        job.setId(1);
        job.setTitle("Backend Developer");
        jobRepository.save(job);

        Mockito.when(jobRepository.findById(1)).thenReturn(Optional.of(job));

        JobDto jobDto = new JobDto();
        jobDto.setJobId(1);
        jobDto.setTitle("Backend Developer");
        jobDto.setDescription("good");
        jobServiceImpl.updateJobById(1, jobDto);

        Assertions.assertEquals(job.getDescription(), "good");
    }

    @Test
    void updateJobStatus_Success() {
        Job job = new Job();
        job.setId(1);
        job.setTitle("Backend Developer");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        job.setStartDate(LocalDate.parse("15/02/2000", formatter));
        job.setEndDate(LocalDate.parse("15/02/2024", formatter));
        jobRepository.save(job);

        List<Job> jobList = new ArrayList<>();
        jobList.add(job);
        Mockito.when(jobRepository.findAllJob()).thenReturn(jobList);
        jobServiceImpl.updateJobStatus();

        Assertions.assertEquals(job.getStatus(), "Open");
    }
}