package fa.training.fjb04.ims.controller.job;

import fa.training.fjb04.ims.dto.candidate.FileDto;
import fa.training.fjb04.ims.dto.job.JobDto;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.repository.common.BenefitRepository;
import fa.training.fjb04.ims.repository.common.LevelRepository;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.repository.candidate.CandidateJobRepository;
import fa.training.fjb04.ims.repository.job.JobBenefitRepository;
import fa.training.fjb04.ims.repository.job.JobLevelRepository;
import fa.training.fjb04.ims.repository.job.JobSkillRepository;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.repository.user.UserScheduleRepository;
import fa.training.fjb04.ims.service.job.JobService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import fa.training.fjb04.ims.service.impl.FileService;
import fa.training.fjb04.ims.util.constant.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final SkillRepository skillRepository;
    private final BenefitRepository benefitRepository;
    private final LevelRepository levelRepository;
    private final JobService jobService;
    private final FileService fileService;

    private final CandidateJobRepository candidateJobRepository;
    private final JobBenefitRepository jobBenefitRepository;
    private final JobLevelRepository jobLevelRepository;
    private final JobSkillRepository jobSkillRepository;
    private final ScheduleService scheduleService;
    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;

    @GetMapping()
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String getAll(Model model) {
        model.addAttribute("jobDtoList", jobService.showAll());
        model.addAttribute("fileDto", new FileDto());
        model.addAttribute("pageSizeCurrent", AppConstants.DEFAULT_PAGE_SIZE);

        return "jobs/list_job";
    }

    @GetMapping("/api/v1")
    @ResponseBody
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Page<JobDto>> searchCandidateList(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false, value = "pageIndex") int pageIndex,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false, value = "pageSize") int pageSize,
            @RequestParam(required = false, value = "keyword") String keyword,
            @RequestParam(required = false, value = "status") String status) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<JobDto> jobDtoPage = jobService.searchJobList(keyword, status, pageable);

        return new ResponseEntity<>(jobDtoPage, HttpStatus.OK);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            fileService.save(file);
            jobService.updateJobStatus();
        }

        return "redirect:/job";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String getFormAddJob(Model model) {
        JobDto jobDto = new JobDto();
        model.addAttribute("addJob", jobDto);
        model.addAttribute("skills", skillRepository.findAllSkill());
        model.addAttribute("levels", levelRepository.findAllLevels());
        model.addAttribute("benefits", benefitRepository.findAllBenefits());

        return "jobs/add_job";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String addJob(@Valid @ModelAttribute("addJob") JobDto jobDto,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {

        if (jobDto.getSalaryTo() == null) {
            bindingResult.rejectValue("salaryTo", "", "Salary can not be blank");
        } else {
            int resultTo = jobDto.getSalaryTo().compareTo(BigDecimal.ZERO);

            if (resultTo < 0) {
                bindingResult.rejectValue("salaryTo", "", "Salary can not be negative");
            }
        }

        if (jobDto.getSalaryFrom() != null) {
            int resultFrom = jobDto.getSalaryFrom().compareTo(BigDecimal.ZERO);
            if (resultFrom < 0) {
                bindingResult.rejectValue("salaryTo", "", "Salary can not be negative");
            } else {
                if (jobDto.getSalaryTo().compareTo(jobDto.getSalaryFrom()) < 0) {
                    bindingResult.rejectValue("salaryTo", "", "Maximum salary must be greater than minimum salary");
                }
            }
        }
        if (jobDto.getStartDate() == null || jobDto.getEndDate() == null) {
            bindingResult.rejectValue("startDate", "", "Start date can not be blank");
            bindingResult.rejectValue("endDate", "", "End date can not be blank");

        } else {
            if (jobDto.getStartDate().isBefore(LocalDate.now())) {
                bindingResult.rejectValue("startDate", "", "The start date must be in the future");
            }
            if (jobDto.getEndDate().isBefore(LocalDate.now())) {
                bindingResult.rejectValue("endDate", "", "The end date must be in the future");
            }
            if (jobDto.getEndDate().isBefore(jobDto.getStartDate())) {
                bindingResult.rejectValue("endDate", "", "The end date must be after the end date");
                bindingResult.rejectValue("startDate", "", "The start date must be before  the end date");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("addJob", jobDto);
            model.addAttribute("skills", skillRepository.findAllSkill());
            model.addAttribute("levels", levelRepository.findAllLevels());
            model.addAttribute("benefits", benefitRepository.findAllBenefits());
            return "jobs/add_job";
        }

        jobService.save(jobDto, userDetails);
        return "redirect:/job";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String editJobForm(@PathVariable Integer id, Model model) {
        JobDto jobDto = jobService.findJobDtoById(id);
        if (jobDto == null) {
            return "/error/404";
        }

        model.addAttribute("jobDto", jobDto);

        model.addAttribute("benefitNames", benefitRepository.findAllName());
        model.addAttribute("levelNames", levelRepository.findAllLevels());
        model.addAttribute("skillNames", skillRepository.findAllSkill());

        System.out.println(jobDto);

        return "jobs/edit_job";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String submitUpdateJob(@PathVariable(name = "id") Integer id,
                                  @ModelAttribute(value = "jobDto") @Valid JobDto jobDto,
                                  BindingResult bindingResult,
                                  Model model) throws IOException {
        jobDto.setJobId(id);

        if (jobDto.getStartDate().isBefore(LocalDate.now())) {
            bindingResult.rejectValue("startDate", "", "The start date must be in the future!");
        }
        if (jobDto.getEndDate().isBefore(LocalDate.now())) {
            bindingResult.rejectValue("endDate", "", "The end date must be in the future!");
        }
        if (jobDto.getEndDate().isBefore(jobDto.getStartDate())) {
            bindingResult.rejectValue("endDate", "", "The end date must be after the start date!");
            bindingResult.rejectValue("startDate", "", "The start date must be before the end date!");
        }
        if (jobDto.getSalaryTo().compareTo(jobDto.getSalaryFrom()) < 0) {
            bindingResult.rejectValue("salaryTo", "", "Maximum salary must be greater than minimum salary!");
        }

        String jobDtoStatus = jobDto.getStatus();

        if (bindingResult.hasErrors()) {
            jobDto.setStatus(jobDtoStatus);
            model.addAttribute("jobDto", jobDto);

            model.addAttribute("benefitNames", benefitRepository.findAllName());
            model.addAttribute("levelNames", levelRepository.findAllLevels());
            model.addAttribute("skillNames", skillRepository.findAllSkill());
            return "jobs/edit_job";
        }

        jobService.updateJobById(id, jobDto);
        return "redirect:/job";
    }

    @GetMapping("/viewDetail/{id}")
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String viewJobDetail(@PathVariable(name = "id") Integer jobId,
                                Model model) {

        Optional<Job> oJob = jobService.findById(jobId);
        if (oJob.isEmpty()) {
            return "/error/404";
        }

        Job job = oJob.get();

        model.addAttribute("jobDetail", job);

        boolean showTodayInfo = job.getCreatedDate().isEqual(LocalDateTime.now());

        boolean showOtherDayInfo = !showTodayInfo;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formatterDate = job.getCreatedDate().format(formatter);


        model.addAttribute("showTodayInfo", showTodayInfo);
        model.addAttribute("showOtherDayInfo", showOtherDayInfo);
        model.addAttribute("formatterDate", formatterDate);

        return "jobs/detail_job";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String deleteJob(@PathVariable(name = "id") Integer id) {

        candidateJobRepository.deleteAllById(id);
        jobBenefitRepository.deleteAllById(id);
        jobLevelRepository.deleteAllById(id);
        jobSkillRepository.deleteAllById(id);

        userScheduleRepository.deleteAllById(id);

        scheduleService.deleteAllByJobId(id);

        jobService.delete(id);

        return "redirect:/job";
    }
}
