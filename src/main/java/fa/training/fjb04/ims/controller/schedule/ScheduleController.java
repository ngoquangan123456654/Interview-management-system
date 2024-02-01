package fa.training.fjb04.ims.controller.schedule;

import fa.training.fjb04.ims.config.security.SecurityUtils;
import fa.training.fjb04.ims.dto.schedule.ScheduleDto;
import fa.training.fjb04.ims.dto.schedule.ScheduleInfoDto;
import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.entity.Schedule;
import fa.training.fjb04.ims.enums.Result;
import fa.training.fjb04.ims.enums.StatusSchedule;
import fa.training.fjb04.ims.repository.candidate.CandidateRepository;
import fa.training.fjb04.ims.repository.job.JobRepository;
import fa.training.fjb04.ims.repository.schedule.ScheduleRepository;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.repository.user.UserScheduleRepository;
import fa.training.fjb04.ims.service.candidate.CandidateService;
import fa.training.fjb04.ims.service.job.JobService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import fa.training.fjb04.ims.service.user.UserService;
import fa.training.fjb04.ims.util.constant.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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

import java.time.Duration;
import java.util.List;

@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final JobRepository jobRepository;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final CandidateService candidateService;
    private final JobService jobService;

    @GetMapping
    @PreAuthorize("hasAnyRole( 'INTERVIEWER','RECRUITER', 'MANAGER', 'ADMIN')")
    public String showScheduleTable(Model model) {

        model.addAttribute("interviewerList", userService.getAllUserNameByRole("INTERVIEWER"));

        return "schedule/schedule_list";
    }

    @GetMapping("/api/v1")
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    @ResponseBody
    public ResponseEntity<Page<ScheduleDto>> searchScheduleTable(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false, value = "pageIndex") int pageIndex
            , @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false, value = "pageSize") int pageSize
            , @RequestParam(required = false, value = "keyword") String keyword
            , @RequestParam(required = false, value = "interviewer") String interviewer
            , @RequestParam(required = false, value = "status") String status) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        StatusSchedule statusSchedule = (status != null) ? StatusSchedule.valueOf(status) : null;
        Page<ScheduleDto> scheduleDtoPage = scheduleService.searchScheduleList(keyword, interviewer, statusSchedule, pageable);

        return new ResponseEntity<>(scheduleDtoPage, HttpStatus.OK);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String showFormAdd(Model model) {
        ScheduleDto scheduleDto = new ScheduleDto();
        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        model.addAttribute("scheduleDto", scheduleDto);
        model.addAttribute("interviewerList", userService.getAllUserNameByRole("INTERVIEWER"));
        model.addAttribute("recruiterList", userService.getAllUserNameByRole("RECRUITER"));
        model.addAttribute("candidateList", candidateService.getAllNoSchedule());
        model.addAttribute("jobList", jobService.getAllTitleByStatus("OPEN"));
        model.addAttribute("userLogin", userLogin);

        return "schedule/schedule_add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String submitAdd(@ModelAttribute("scheduleDto") @Valid ScheduleDto scheduleDto
            , BindingResult bindingResult
            , Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("scheduleDto", scheduleDto);

            model.addAttribute("interviewerList", userService.getAllUserNameByRole("INTERVIEWER"));
            model.addAttribute("recruiterList", userService.getAllUserNameByRole("RECRUITER"));
            model.addAttribute("candidateList", candidateService.getAllNoSchedule());
            model.addAttribute("jobList", jobService.getAllTitleByStatus("OPEN"));


            return "schedule/schedule_add";
        }

        scheduleDto.setMainInterviewer(scheduleDto.getInterviewerList().get(0));

        if (scheduleDto.getScheduleTimeTo().isBefore(scheduleDto.getScheduleTimeFrom())) {
            bindingResult.rejectValue("scheduleTimeFrom", "", "Schedule time from must be before the schedule time to");
            if (bindingResult.hasErrors()) {
                model.addAttribute("scheduleDto", scheduleDto);

                model.addAttribute("interviewerList", userService.getAllUserNameByRole("INTERVIEWER"));
                model.addAttribute("recruiterList", userService.getAllUserNameByRole("RECRUITER"));
                model.addAttribute("candidateList", candidateService.getAllNoSchedule());
                model.addAttribute("jobList", jobService.getAllTitleByStatus("OPEN"));

                return "schedule/schedule_add";
            }
        }
//
//        for(String interviewer : scheduleDto.getInterviewerList()){
//            if(interviewer.equals(scheduleDto.getMainInterviewer())){
//                bindingResult.rejectValue("mainInterviewer","","The main interviewer cannot be on the interviewer list");
//            }
//        }


        scheduleService.save(scheduleDto);
        return "redirect:/schedule";
    }


    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String viewCandidateInfo(@PathVariable Integer id, Model model) {
        Schedule schedule = scheduleService.findById(id);

        ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();

        BeanUtils.copyProperties(schedule, scheduleInfoDto);
        scheduleInfoDto.setCandidate(schedule.getCandidate() != null ? schedule.getCandidate() : new Candidate());
        scheduleInfoDto.setJob(schedule.getJob().getTitle());
        scheduleInfoDto.setRecruiter(schedule.getRecruiter().getFullName());
        scheduleInfoDto.setMainInterviewer(schedule.getMainInterviewer() != null ? schedule.getMainInterviewer().getFullName() : "null");
        scheduleInfoDto.setInterviewerList(userService.getAllNameByScheduleId(id));
        scheduleInfoDto.setResult(schedule.getResult());
        scheduleInfoDto.setStatus(schedule.getStatusSchedule().getName());
        scheduleInfoDto.setScheduleId(schedule.getId());
        model.addAttribute("scheduleDto", scheduleInfoDto);

        return "schedule/schedule_detail";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyRole( 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String getUpdateSchedule(@PathVariable Integer id,
                                    Model model) {
        ScheduleDto scheduleDto = new ScheduleDto();
        Schedule schedule = scheduleRepository.findByScheduleId(id);

        BeanUtils.copyProperties(schedule, scheduleDto);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        scheduleDto.setScheduleId(schedule.getId());
        scheduleDto.setJob(schedule.getJob().getTitle());
        scheduleDto.setCandidate(schedule.getCandidate() != null ? schedule.getCandidate() : new Candidate());
        scheduleDto.setMainInterviewer(schedule.getMainInterviewer() != null ? schedule.getMainInterviewer().getUserName() : "null");
        scheduleDto.setRecruiter(schedule.getRecruiter().getUserName());
        scheduleDto.setInterviewerList(userService.getAllNameByScheduleId(id));

        List<Candidate> candidateList = candidateRepository.findAllCandidateNoSchedule();

        if (schedule.getCandidate() != null) {
            candidateList.add(schedule.getCandidate());
        }

        model.addAttribute("userLogin", userLogin);
        model.addAttribute("candidateList", candidateList);
        model.addAttribute("jobList", jobService.getAllTitleByStatus("OPEN"));
        model.addAttribute("interviewerList", userRepository.findAllInterviewer());
        model.addAttribute("recruiterList", userRepository.findAllRecruiter());
        model.addAttribute("scheduleUpdate", scheduleDto);
        return "schedule/schedule_edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole( 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String updateSchedule(@PathVariable Integer id,
                                 @Valid @ModelAttribute(value = "scheduleUpdate") ScheduleDto scheduleDto,
                                 BindingResult bindingResult,
                                 Model model) {

        if (scheduleDto.getScheduleTimeTo().isBefore(scheduleDto.getScheduleTimeFrom())) {
            bindingResult.rejectValue("scheduleTimeFrom", "", "Schedule time from must be before the schedule time to");
        } else {
            if (scheduleDto.getScheduleTimeFrom().isAfter(scheduleDto.getScheduleTimeTo().minusMinutes(30))) {
                bindingResult.rejectValue("scheduleTimeFrom", "", "Minimum interview time is 30 minutes");
            }
        }
        if (scheduleDto.getMainInterviewer().isEmpty()) {
            bindingResult.rejectValue("mainInterviewer", "", "The main interviewer cannot be empty");
        } else {

            for (String interviewer : scheduleDto.getInterviewerList()) {
                if (interviewer.equals(scheduleDto.getMainInterviewer())) {
                    bindingResult.rejectValue("mainInterviewer", "", "The main interviewer cannot be on the interviewer list");
                }
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("candidateList", candidateRepository.findAllCandidate());
            model.addAttribute("jobList", jobService.getAllTitleByStatus("OPEN"));
            model.addAttribute("interviewerList", userRepository.findAllInterviewer());
            model.addAttribute("recruiterList", userRepository.findAllRecruiter());
            model.addAttribute("scheduleUpdate", scheduleDto);
            return "schedule/schedule_edit";
        }

        scheduleDto.setScheduleId(id);
        scheduleService.updateSchedule(scheduleDto);
        return "redirect:/schedule";
    }

    @GetMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole( 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String cancelInterview(@PathVariable Integer id) {
        Schedule schedule = scheduleRepository.findByScheduleId(id);
        if (schedule == null) {
            return "/error/404";
        }

        schedule.setStatusSchedule(StatusSchedule.CANCELLED);
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }

    @PostMapping("/submit/{id}")
    @PreAuthorize("hasAnyRole('INTERVIEWER')")
    public String submitResult(@PathVariable Integer id,
                               @Valid @ModelAttribute(value = "scheduleDto") ScheduleInfoDto scheduleInfoDto,
                               @AuthenticationPrincipal UserDetails userDetails,
                               BindingResult bindingResult,
                               Model model) {
        Schedule schedule = scheduleService.findById(id);
        scheduleInfoDto.setMainInterviewer(schedule.getMainInterviewer().getUserName());
        if (!userDetails.getUsername().equals(scheduleInfoDto.getMainInterviewer())) {
            bindingResult.rejectValue("submit", "", "You do not have permission to use this action");
        }
        if (bindingResult.hasErrors()) {
            BeanUtils.copyProperties(schedule, scheduleInfoDto);
            scheduleInfoDto.setCandidate(schedule.getCandidate());
            scheduleInfoDto.setJob(schedule.getJob().getTitle());
            scheduleInfoDto.setRecruiter(schedule.getRecruiter().getFullName());
            scheduleInfoDto.setMainInterviewer(schedule.getMainInterviewer() != null ? schedule.getMainInterviewer().getFullName() : "null");
            scheduleInfoDto.setInterviewerList(userService.getAllNameByScheduleId(id));
            scheduleInfoDto.setResult(schedule.getResult());
            scheduleInfoDto.setStatus(schedule.getStatusSchedule().getName());
            scheduleInfoDto.setScheduleId(schedule.getId());
            model.addAttribute("scheduleDto", scheduleInfoDto);

            return "schedule/schedule_detail";
        }
        schedule.setResult(scheduleInfoDto.getResult());
        schedule.setNote(scheduleInfoDto.getNote());
        scheduleRepository.save(schedule);
        return "redirect:/schedule";
    }
}
