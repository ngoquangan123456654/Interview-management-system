package fa.training.fjb04.ims.controller.candidate;

import fa.training.fjb04.ims.config.security.SecurityUtils;
import fa.training.fjb04.ims.dto.candidate.CandidateDto;
import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.entity.Schedule;
import fa.training.fjb04.ims.entity.common.File;
import fa.training.fjb04.ims.entity.intermediateTable.CandidateSkill;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.Position;
import fa.training.fjb04.ims.enums.Status;
import fa.training.fjb04.ims.repository.candidate.CandidateJobRepository;
import fa.training.fjb04.ims.repository.common.FileRepository;
import fa.training.fjb04.ims.repository.common.SkillRepository;
import fa.training.fjb04.ims.service.candidate.CandidateService;
import fa.training.fjb04.ims.service.candidate.CandidateSkillService;
import fa.training.fjb04.ims.service.common.FileStorageService;
import fa.training.fjb04.ims.service.common.HighLevelService;
import fa.training.fjb04.ims.service.common.SkillService;
import fa.training.fjb04.ims.service.offer.OfferService;
import fa.training.fjb04.ims.service.schedule.JobScheduleService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import fa.training.fjb04.ims.service.schedule.UserScheduleSevice;
import fa.training.fjb04.ims.service.user.UserService;
import fa.training.fjb04.ims.util.constant.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final HighLevelService highLevelService;
    private final SkillService skillService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final CandidateSkillService candidateSkillService;
    private final SkillRepository skillRepository;
    private final CandidateJobRepository candidateJobRepository;
    private final FileRepository fileRepository;
    private final OfferService offerService;
    private final ScheduleService scheduleService;
    private final UserScheduleSevice userScheduleSevice;
    private final JobScheduleService jobScheduleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String getCandidateList() {
        return "candidates/list_candidate";
    }

    @GetMapping("/api/v1")
    @ResponseBody
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Page<CandidateDto>> searchCandidateList(
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false, value = "pageNo") int pageIndex,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false, value = "pageSize") int pageSize,
            @RequestParam(required = false, value = "keyword") String keyword,
            @RequestParam(required = false, value = "status") String status) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<CandidateDto> candidateDtoPage = candidateService.searchCandidateList(keyword, status, pageable);

        return new ResponseEntity<>(candidateDtoPage, HttpStatus.OK);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String addCandidateForm(Model model) {


        List<String> skillsList = skillService.findAllSkill();
        model.addAttribute("skills", skillsList);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        model.addAttribute("userLogin", userLogin);

        List<String> highLevels = highLevelService.findAllHighLevel();
        model.addAttribute("highLevels", highLevels);

        model.addAttribute("recruiters", userService.findAllRecruiter());

        model.addAttribute("candidateAdd", new CandidateDto());

        model.addAttribute("submit", null);

        return "candidates/add_candidate";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String addCandidate
            (@Valid @ModelAttribute("candidateAdd") CandidateDto candidateDto,
             BindingResult bindingResult,
             @AuthenticationPrincipal UserDetails userDetails,
             Model model
            )
            throws IOException {
        System.out.println(candidateDto.getCvAttachment());
        //Todo: define error 2a
        if (candidateDto.getFullName() == null || candidateDto.getFullName().trim().isEmpty()) {
            bindingResult.rejectValue("fullName", "ME002");
        }

        if (candidateDto.getCvAttachment().getSize() <= 0) {
            bindingResult.rejectValue("cvAttachment", "", "CV Attachment can not empty");
        } else {
            String[] allowedExtensions = {"docx", "dot", "pdf"};
            Boolean isAllowedExtension = false;
            for (String allowedExtension : allowedExtensions) {
                if (FilenameUtils.getExtension(candidateDto.getCvAttachment().getOriginalFilename()).equals(allowedExtension)) {
                    isAllowedExtension = true;
                }
            }
            if (!isAllowedExtension) {
                bindingResult.rejectValue("cvAttachment", "", "File is not supported on this website");
            }

            long fileSizeLimit = 100 * 1024 * 1024;
            if (candidateDto.getCvAttachment().getSize() > fileSizeLimit) {
                bindingResult.rejectValue("cvAttachment", "", "The file size is too large, please choose a file with a smaller size");
            }
        }
        if (candidateDto.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "", "Email can not be empty");
        }
        if (candidateDto.getGender().isEmpty()) {
            bindingResult.rejectValue("gender", "", "Gender can not be empty");
        }
        if (candidateDto.getPosition().isEmpty()) {
            bindingResult.rejectValue("position", "", "Position can not be empty");
        }
        if (candidateDto.getSkill().size() <= 0) {
            bindingResult.rejectValue("skill", "", "Skill can not be empty");
        }
        if (candidateDto.getUser().isEmpty()) {
            bindingResult.rejectValue("user", "", "Recruiter can not be empty");
        }
        if (candidateDto.getStatus().isEmpty()) {
            bindingResult.rejectValue("status", "", "Status can not be empty");
        }
        if (candidateDto.getHighLevel().isEmpty()) {
            bindingResult.rejectValue("highLevel", "", "High level can not be empty");
        }

        //TODO: define error 2b
        if (candidateService.existsByPhoneNumber(candidateDto.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "", "Already exists phone number");
        }

        if (candidateService.existsByEmail(candidateDto.getEmail())) {
            bindingResult.rejectValue("email", "", "Already exists email ");
        }


        if (bindingResult.hasErrors()) {

            bindingResult.rejectValue("submit", "ME011");

            String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
            model.addAttribute("userLogin", userLogin);

            List<String> skillsList = skillService.findAllSkill();
            model.addAttribute("skills", skillsList);

            List<String> highLevels = highLevelService.findAllHighLevel();
            model.addAttribute("highLevels", highLevels);

            model.addAttribute("recruiters", userService.findAllRecruiter());

            return "/candidates/add_candidate";
        }

        //save Candidate
        Candidate candidate = new Candidate();

        //store Enums:
        candidate.setStatus(Status.fromValue(candidateDto.getStatus()));
        candidate.setGender(Gender.fromValue(candidateDto.getGender()));
        candidate.setPosition(Position.fromValue(candidateDto.getPosition()));


        BeanUtils.copyProperties(candidateDto, candidate);


        candidate.setUser(userService.findByUserName(candidateDto.getUser()));

        // Thêm các CandidateSkill vào danh sách của candidate
        List<String> skillsList = candidateDto.getSkill();
        for (String skill : skillsList) {
            CandidateSkill candidateSkill = new CandidateSkill();
            candidateSkill.setSkill(skillService.findByName(skill));
            candidate.getCandidateSkillList().add(candidateSkill);
        }
        //file candidate.
        File file = new File();
        if (candidateDto.getCvAttachment() != null && candidateDto.getCvAttachment().getSize() > 0) {

            String originalFilename = candidateDto.getCvAttachment().getOriginalFilename();
            String fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid + fileExtension;

            // Đường dẫn đầy đủ của tệp lưu trữ
            Path targetPath = Path.of("C:\\Users\\DUC DUC\\Desktop\\Mock\\Git\\mock_ims_team_01\\uploads\\file", uniqueFileName);
            file.setUrl("file/" + uniqueFileName);
            file.setName(candidateDto.getCvAttachment().getOriginalFilename());
            Files.copy(candidateDto.getCvAttachment().getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        }
        candidate.setFile(file);

        //convert highlevel
        candidate.setHighLevel(highLevelService.findByName(candidateDto.getHighLevel()));

        //using temperature when not jet open security.
        candidate.setCreatedBy(userDetails.getUsername());
        candidate.setCreatedDate(LocalDateTime.now());
        candidate.setLastModifiedBy(userDetails.getUsername());
        candidate.setLastModifiedDate(LocalDateTime.now());

        candidateService.save(candidate);

        for (CandidateSkill candidateSkill : candidate.getCandidateSkillList()) {
            candidateSkill.setCandidate(candidate);
        }

        candidateSkillService.saveAll(candidate.getCandidateSkillList());

        return "redirect:/candidate";
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String viewCandidateInfo(@PathVariable Integer id, Model model) {
        Optional<Candidate> oCandidate = candidateService.findById(id);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();
        CandidateDto candidateDto = new CandidateDto();

        BeanUtils.copyProperties(candidate, candidateDto);
        candidateDto.setUser(candidate.getUser().getFullName());
        candidateDto.setSkill(skillRepository.findAllByCandidateId(id));
        candidateDto.setHighLevel(candidate.getHighLevel().getName());
        candidateDto.setGender(candidate.getGender().getName());
        candidateDto.setPosition(candidate.getPosition().getName());
        candidateDto.setStatus(candidate.getStatus().getName());
        candidateDto.setCreatedDate(candidate.getCreatedDate());
        candidateDto.setCreatedBy(candidate.getCreatedBy());

        String role = Objects.requireNonNull(SecurityUtils.getCurrentRole()).toString();

        if (!candidate.getStatus().getName().equals("Banned") && !role.equalsIgnoreCase("[ROLE_INTERVIEWER]")) {
            model.addAttribute("banned", "Ban Candidate");
        }

        model.addAttribute("files", candidate.getFile().getName());
        model.addAttribute("fileUrl", candidate.getFile().getUrl());
        model.addAttribute("candidateDto", candidateDto);

        return "candidates/detail_candidate";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String getUpdateForm(@PathVariable(name = "id") Integer id,
                                Model model,
                                MultipartFile file) {

        CandidateDto candidateDto = new CandidateDto();

        Optional<Candidate> oCandidate = candidateService.findById(id);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();
        BeanUtils.copyProperties(candidate, candidateDto);
        candidateDto.setSkill(skillService.findAllByCandidateId(id));
        candidateDto.setHighLevel(candidate.getHighLevel().getName());
        candidateDto.setGender(candidate.getGender().getName());
        candidateDto.setPosition(candidate.getPosition().getName());
        candidateDto.setStatus(candidate.getStatus().getName());
        candidateDto.setUser(candidate.getUser().getUserName());
        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);


        model.addAttribute("userLogin", userLogin);
        model.addAttribute("skills", skillService.findAllSkill());
        model.addAttribute("recruiters", userService.findAllRecruiter());
        model.addAttribute("highLevels", highLevelService.findAllHighLevel());
        if (candidate.getFile() != null) {
            model.addAttribute("files", candidate.getFile().getName());
        }

        model.addAttribute("candidateUpdate", candidateDto);
        return "candidates/edit_candidate";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String updateCandidate(@PathVariable(name = "id") Integer id,
                                  @Valid @ModelAttribute(value = "candidateUpdate") CandidateDto candidateDto,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) throws IOException {
        Optional<Candidate> oCandidate = candidateService.findById(id);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();
        candidateDto.setCandidateId(id);
        if (candidateDto.getFullName() == null || candidateDto.getFullName().trim().isEmpty()) {
            bindingResult.rejectValue("fullName", "", "Full name can not ve empty");
        }

        if (candidateDto.getEmail().isEmpty()) {
            bindingResult.rejectValue("email", "", "Email can not be empty");
        }
        if (candidateDto.getGender().isEmpty()) {
            bindingResult.rejectValue("gender", "", "Gender can not be empty");
        }
        if (candidateDto.getPosition().isEmpty()) {
            bindingResult.rejectValue("position", "", "Position can not be empty");
        }
        if (candidateDto.getSkill().size() <= 0) {
            bindingResult.rejectValue("skill", "", "Skill can not be empty");
        }
        if (candidateDto.getUser().isEmpty()) {
            bindingResult.rejectValue("user", "", "Recruiter can not be empty");
        }
        if (candidateDto.getStatus().isEmpty()) {
            bindingResult.rejectValue("status", "", "Status can not be empty");
        }
        if (candidateDto.getHighLevel().isEmpty()) {
            bindingResult.rejectValue("highLevel", "", "High level can not be empty");
        }

        if (candidateService.existsByPhoneNumber(candidateDto.getPhoneNumber()) && !candidateService.existsByPhoneNumberAndCandidateId(candidateDto.getPhoneNumber(), candidateDto.getCandidateId())) {
            bindingResult.rejectValue("phoneNumber", "", "Phone number has already exist");
        }

        if (candidateService.existsByEmail(candidateDto.getEmail()) && !candidateService.existsByEmailAndCandidateId(candidateDto.getEmail(), candidateDto.getCandidateId())) {
            bindingResult.rejectValue("email", "", "Email has already exist");
        }

        if (Objects.isNull(candidate.getFile()) && candidateDto.getCvAttachment().getSize() <= 0) {
            bindingResult.rejectValue("cvAttachment", "", "CV Attachment can not be empty");
        }


        if (candidateDto.getCvAttachment().getSize() >= 1) {
            String[] allowedExtensions = {"docx", "dot", "pdf"};
            Boolean isAllowedExtension = false;
            for (String allowedExtension : allowedExtensions) {
                if (FilenameUtils.getExtension(candidateDto.getCvAttachment().getOriginalFilename()).equals(allowedExtension)) {
                    isAllowedExtension = true;
                }
            }
            if (!isAllowedExtension) {
                bindingResult.rejectValue("cvAttachment", "", "File is not supported on this website");
            }

            long fileSizeLimit = 100 * 1024 * 1024;
            if (candidateDto.getCvAttachment().getSize() > fileSizeLimit) {
                bindingResult.rejectValue("cvAttachment", "", "The file size is too large, please choose a file with a smaller size");
            }
        }


        if (bindingResult.hasErrors()) {
            String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
            model.addAttribute("userLogin", userLogin);
            bindingResult.rejectValue("submit", "ME013");
            model.addAttribute("skills", skillService.findAllSkill());
            model.addAttribute("recruiters", userService.findAllRecruiter());
            model.addAttribute("highLevels", highLevelService.findAllHighLevel());
            if (candidate.getFile() != null) {
                model.addAttribute("files", candidate.getFile().getName());
            }
            return "candidates/edit_candidate";
        }

        String fileUrl = null;
        File file;
        if (candidate.getFile() == null) {
            file = new File();
            fileRepository.save(file);
            candidate.setFile(file);
        } else {
            file = candidate.getFile();
        }
        if (candidateDto.getCvAttachment() != null && candidate.getFile().getName() == null && candidate.getFile().getUrl() == null) {
//            fileUrl = fileStorageService.save(candidateDto.getCvAttachment(), "file");
//            file.setName(candidateDto.getCvAttachment().getOriginalFilename());
//            file.setUrl(fileUrl);
//            fileRepository.save(file);


            String originalFilename = candidateDto.getCvAttachment().getOriginalFilename();
            String fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid + fileExtension;

            // Đường dẫn đầy đủ của tệp lưu trữ
            Path targetPath = Path.of("C:\\Users\\DUC DUC\\Desktop\\Mock\\Git\\mock_ims_team_01\\uploads\\file", uniqueFileName);
            file.setUrl("file/" + uniqueFileName);
            file.setName(candidateDto.getCvAttachment().getOriginalFilename());
            Files.copy(candidateDto.getCvAttachment().getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        candidateService.updateCandidate(candidateDto, userDetails);
        return "redirect:/candidate";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String deleteCandidate(@RequestParam Integer candidateId) {
        Optional<Candidate> oCandidate = candidateService.findById(candidateId);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();

        //TODO:CHECK

        //delete all schedule
        userScheduleSevice.deleteAllUserScheduleById(candidateId);
        jobScheduleService.deleteAllJobScheduleServiceById(candidateId);
        scheduleService.deleteById(candidateId);

        offerService.deleteByCandidateId(candidateId);

        candidateSkillService.deleteByCandidateId(candidateId);
        candidateJobRepository.deleteAllByCandidate(candidateId);
        candidateService.deleteById(candidateId);
        if (candidate.getFile().getUrl() != null) {
            fileStorageService.delete(candidate.getFile().getUrl());
        }
        return "redirect:/candidate";
    }

    @GetMapping("/{id}/files/delete")
    @PreAuthorize("hasAnyRole('INTERVIEWER', 'RECRUITER', 'MANAGER', 'ADMIN')")
    public String deleteFile(@PathVariable(name = "id") Integer id,
                             @ModelAttribute(value = "candidateUpdate") CandidateDto candidateDto) {

        Optional<Candidate> oCandidate = candidateService.findById(id);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();
        File file = candidate.getFile();
        fileStorageService.delete(file.getUrl());
        file.setName(null);
        file.setUrl(null);
        fileRepository.save(file);
        candidateService.save(candidate);

        return "redirect:/candidate/update/{id}";
    }

    @GetMapping("/ban/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String banCandidate(@PathVariable Integer id) {
        Optional<Candidate> oCandidate = candidateService.findById(id);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();
        candidate.setStatus(Status.BANNED);

        candidateService.save(candidate);

        return "redirect:/candidate";
    }

    @GetMapping("/open/{id}")
    public String openCandidate(@PathVariable Integer id) {
        Optional<Candidate> oCandidate = candidateService.findById(id);
        if (oCandidate.isEmpty()) {
            return "/error/404";
        }

        Candidate candidate = oCandidate.get();
        candidate.setStatus(Status.OPEN);

        candidateService.save(candidate);

        return "redirect:/candidate";
    }

}
