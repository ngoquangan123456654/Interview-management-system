package fa.training.fjb04.ims.controller.offer;
import fa.training.fjb04.ims.config.security.SecurityUtils;
import fa.training.fjb04.ims.dto.offerDto.OfferDTO;
import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.entity.Schedule;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.entity.common.Level;
import fa.training.fjb04.ims.enums.*;
import fa.training.fjb04.ims.service.candidate.CandidateService;
import fa.training.fjb04.ims.service.common.LevelService;
import fa.training.fjb04.ims.service.excel.ExcelService;
import fa.training.fjb04.ims.service.offer.OfferService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import fa.training.fjb04.ims.service.schedule.UserScheduleSevice;
import fa.training.fjb04.ims.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/offer")
public class OfferController {
    private final LevelService levelService;
    private final UserService userService;
    private final OfferService offerService;
    private final ScheduleService scheduleService;
    private final UserScheduleSevice userScheduleService;
    private final CandidateService candidateService;
    private final ExcelService excelService;

    @GetMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String getOfferList() {
        return "offer/list_offer";
    }

    @GetMapping("/add/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String addOfferShowForm(@PathVariable Integer id,
                                   Model model) {
        /*model.addAttribute("positions", Position.values());*/
        Candidate candidateFindById = candidateService.findById(id).get();
        model.addAttribute("candidateFind", candidateFindById);

        List<User> userOfferList = userScheduleService.getUserByScheduleId(id);
        model.addAttribute("listUser", userOfferList);

        Schedule scheduleDetail = scheduleService.findById(id);
        model.addAttribute("scheduleDetail", scheduleDetail);

        List<String> approvers = userService.userHaveUserManagerRole("MANAGER");
        model.addAttribute("approvers", approvers);

        List<String> recruiterOwners = userService.userHaveUserRecruiterRole("RECRUITER");
        model.addAttribute("recruiterOwners", recruiterOwners);

        List<String> levels = levelService.findAllLevels();
        model.addAttribute("levels", levels);

        //Main Interview
        User mainInterview = scheduleService.findMainInterviewByScheduleId(id);
        model.addAttribute("mainIntervew", mainInterview);

        model.addAttribute("offerAdd", new OfferDTO());
        return "offer/add_offer";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String addOffer(OfferDTO offerDTO){

        Offer offer = new Offer();
        offer.setPosition(Position.valueOf(offerDTO.getPosition()));
        offer.setContractType(ContractType.valueOf(offerDTO.getContractType()));
        offer.setDepartment(Department.valueOf(offerDTO.getDepartment()));

        //find Approver:
        User userApprover = userService.findByUserName(offerDTO.getApprover());
        offer.setApprover(userApprover);

        //find Recruiter Owner:
        User userRecruiter = userService.findByUserName(offerDTO.getRecuiterOwner());
        offer.setRecruiter(userRecruiter);

        //find Level
        Level level = levelService.findLevelByName(offerDTO.getLevel());
        offer.setLevel(level);

        //set status:
        offer.setStatus(Status.fromCode(Status.WAITING_FOR_APPROVAL.getCode()));

        //convert Basic Salary:
        String basicsalary = offerDTO.getBasicSalary();
        BigDecimal convertSalary = BigDecimal.valueOf(Double.valueOf(basicsalary));
        offer.setBasicSalary(convertSalary);

        BeanUtils.copyProperties(offerDTO, offer);

        Offer offerHaveSave = offerService.saveTest(offer);

        Integer idFind = offerHaveSave.getId();

        Schedule scheduleOffer = scheduleService.findById(idFind);
        Candidate candidateOffer = candidateService.findById(idFind).get();

//        set Status Candidate
        candidateOffer.setStatus(Status.fromCode(Status.WAITING_FOR_APPROVAL.ordinal()));
        candidateService.save(candidateOffer);
        offer.setCandidate(candidateOffer);
        offer.setSchedule(scheduleOffer);

        offerService.save(offer);

        return "redirect:/offer";

    }
    //    Edit Offer
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String showEditOfferById(@PathVariable Integer id, Model model){

        //TODO:EDIT
        Optional<Offer> offerOptional = offerService.findById(id);
        Offer editOffer = offerOptional.get();

        OfferDTO editOfferDTO = new OfferDTO();

        /*editOfferDTO.setContractPeriodFrom(editOffer.getContractPeriodFrom());
        editOfferDTO.setContractPeriodTo(editOffer.getContractPeriodTo());
        editOfferDTO.setDueDate(editOffer.getDueDate());*/

        editOfferDTO.setBasicSalary(String.valueOf(editOffer.getBasicSalary()));
        BeanUtils.copyProperties(editOffer, editOfferDTO);
        model.addAttribute("editOfferDTO", editOfferDTO);

        //TODO:EDIT
        //ren title
        Schedule editSchedule = scheduleService.findById(id);

        //Main Interview
        User mainInterview = scheduleService.findMainInterviewByScheduleId(id);

        //List sub interview
        List<User> userOfferList = userScheduleService.getUserByScheduleId(id);

        List<String> approvers = userService.userHaveUserManagerRole("MANAGER");
        model.addAttribute("approvers", approvers);

        List<String> levels = levelService.findAllLevels();
        model.addAttribute("levels", levels);

        List<String> recruiterOwners = userService.userHaveUserRecruiterRole("RECRUITER");
        model.addAttribute("recruiterOwners", recruiterOwners);

        model.addAttribute("listUser", userOfferList);
        model.addAttribute("editOffer", editOffer);
        model.addAttribute("mainIntervew", mainInterview);
        model.addAttribute("editSchedule", editSchedule);

        return "offer/edit_offer";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String editOffer(@PathVariable Integer id,
                            @ModelAttribute("editOfferDTO") OfferDTO editOfferDTO,
                            BindingResult bindingResult,
                            Model model){
        if (editOfferDTO.getPosition().isEmpty()) {
            bindingResult.rejectValue("position", "", "Position can not be empty");
        }
        if (editOfferDTO.getApprover().isEmpty()) {
            bindingResult.rejectValue("approver", "", "Approver can not be empty");
        }
        if (editOfferDTO.getContractPeriodFrom() == null || editOfferDTO.getContractPeriodTo() == null) {
            bindingResult.rejectValue("contractPeriodFrom", "", "Contract period can not be empty");
        } else {
            if (editOfferDTO.getContractPeriodFrom().isAfter(editOfferDTO.getContractPeriodTo())) {
                bindingResult.rejectValue("contractPeriodFrom", "", "Contract period from can not be after contract period to");
            }
        }
        if (editOfferDTO.getContractType().isEmpty()) {
            bindingResult.rejectValue("contractType", "", "Contract type can not be empty");
        }
        if (editOfferDTO.getLevel().isEmpty()) {
            bindingResult.rejectValue("level", "", "Level can not be empty");
        }
        if (editOfferDTO.getDepartment().isEmpty()) {
            bindingResult.rejectValue("department", "", "Department can not be empty");
        }
        if (editOfferDTO.getRecuiterOwner().isEmpty()) {
            bindingResult.rejectValue("recuiterOwner", "", "Recruiter can not be empty ");
        }
        if (editOfferDTO.getDueDate() == null) {
            bindingResult.rejectValue("dueDate", "", "Due date can not be empty");
        }
        if (editOfferDTO.getBasicSalary().isEmpty()) {
            bindingResult.rejectValue("basicSalary", "", "Basic salary can not be empty");
        }
        if (bindingResult.hasErrors()) {

            model.addAttribute("editOfferDTO", editOfferDTO);

            Optional<Offer> offerOptional = offerService.findById(id);
            Offer editOffer = offerOptional.get();

            //ren title
            Schedule editSchedule = scheduleService.findById(id);

            //Main Interview
            User mainInterview = scheduleService.findMainInterviewByScheduleId(id);

            //List sub interview
            List<User> userOfferList = userScheduleService.getUserByScheduleId(id);

            List<String> approvers = userService.userHaveUserManagerRole("MANAGER");
            model.addAttribute("approvers", approvers);

            List<String> levels = levelService.findAllLevels();
            model.addAttribute("levels", levels);

            List<String> recruiterOwners = userService.userHaveUserRecruiterRole("RECRUITER");
            model.addAttribute("recruiterOwners", recruiterOwners);

            model.addAttribute("listUser", userOfferList);
            model.addAttribute("editOffer", editOffer);
            model.addAttribute("mainIntervew", mainInterview);
            model.addAttribute("editSchedule", editSchedule);

            return "offer/edit_offer";
        }

        Offer existingOffer = offerService.findById(id).get();

        existingOffer.setPosition(Position.valueOf(editOfferDTO.getPosition()));
        existingOffer.setContractType(ContractType.valueOf(editOfferDTO.getContractType()));
        existingOffer.setDepartment(Department.valueOf(editOfferDTO.getDepartment()));

        //find Approver:
        User userApprover = userService.findByUserName(editOfferDTO.getApprover());
        existingOffer.setApprover(userApprover);

        //find Recruiter Owner:
        User userRecruiter = userService.findByUserName(editOfferDTO.getRecuiterOwner());
        existingOffer.setRecruiter(userRecruiter);

        //find Level
        Level level = levelService.findLevelByName(editOfferDTO.getLevel());
        existingOffer.setLevel(level);

        //convert Basic Salary:
        String basicsalary = editOfferDTO.getBasicSalary();
        BigDecimal convertSalary = BigDecimal.valueOf(Double.valueOf(basicsalary));
        existingOffer.setBasicSalary(convertSalary);

        BeanUtils.copyProperties(editOfferDTO, existingOffer);
        offerService.save(existingOffer);

        return "redirect:/offer";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String viewOfferDetail(@PathVariable Integer id,
                                  Model model) {

        Optional<Offer> offerOptional = offerService.findById(id);
        Offer detailOffer = offerOptional.get();

        String role = SecurityUtils.getCurrentRole().toString();

        String userLogin = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (userLogin != null) {
            String userDepartment = userService.findByUserName(userLogin).getDepartment().getName();

            if (role.equals("[ROLE_MANAGER]") || role.equals("[ROLE_ADMIN]")) {
                if (detailOffer.getStatus().equals(Status.WAITING_FOR_APPROVAL)) {
                    model.addAttribute("approveButton", "Approve");
                    model.addAttribute("rejectButton", "Reject");
                    model.addAttribute("editButton", "Edit");
                }
                if (detailOffer.getStatus().equals(Status.APPROVED_OFFER)) {
                    model.addAttribute("sendButton", "Mark as Sent to Candidate");
                }
            }

            if ( (role.equals("[ROLE_MANAGER]") && userDepartment.equalsIgnoreCase("HR")) || role.equals("[ROLE_ADMIN]")
                    || role.equals("[ROLE_RECRUITER]") && !detailOffer.getStatus().equals(Status.CANCELLED_OFFER)) {
                model.addAttribute("cancelButton", "Cancel Offer");
            }

            if (detailOffer.getStatus().equals(Status.WAITING_FOR_RESPONSE)) {
                model.addAttribute("sentNotice", "Offer has been already sent to Candidate");
            }
        }
        //ren title
        Schedule scheduleDetail = scheduleService.findById(id);

        //Main Interview
        User mainInterview = scheduleService.findMainInterviewByScheduleId(id);
        model.addAttribute("mainIntervew", mainInterview);

        //List sub interview
        List<User> userOfferList = userScheduleService.getUserByScheduleId(id);
        model.addAttribute("listUser", userOfferList);

        model.addAttribute("detailOffer", detailOffer);
        model.addAttribute("scheduleDetail", scheduleDetail);

        return "offer/detail_offer";
    }

    @GetMapping("/view/approve-offer/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String approveOffer(@PathVariable Integer id) {
        Optional<Offer> offerOptional = offerService.findById(id);
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (Objects.isNull(offerOptional) || Objects.isNull(candidateOptional)) {
            return "error/404";
        }
        Offer detailOffer = offerOptional.get();
        Candidate candidateOffer = candidateOptional.get();

        if (detailOffer != null && candidateOffer != null) {
            detailOffer.setStatus(Status.fromCode(Status.APPROVED_OFFER.getCode()));
            candidateOffer.setStatus(Status.fromCode(Status.APPROVED_OFFER.getCode()));
            offerService.save(detailOffer);
            candidateService.save(candidateOffer);
        }

        return "redirect:/offer/view/" + id;
    }

    @GetMapping("/view/reject-offer/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String rejectOffer(@PathVariable Integer id) {
        Optional<Offer> offerOptional = offerService.findById(id);
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (Objects.isNull(offerOptional) || Objects.isNull(candidateOptional)) {
            return "error/404";
        }
        Offer detailOffer = offerOptional.get();
        Candidate candidateOffer = candidateOptional.get();

        if (detailOffer != null && candidateOffer != null) {
            detailOffer.setStatus(Status.fromCode(Status.REJECTED_OFFER.getCode()));
            candidateOffer.setStatus(Status.fromCode(Status.REJECTED_OFFER.getCode()));
            offerService.save(detailOffer);
            candidateService.save(candidateOffer);
        }
        return "redirect:/offer/view/" + id;
    }

    @GetMapping("/view/send-offer/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String sendOffer(@PathVariable Integer id) {
        Optional<Offer> offerOptional = offerService.findById(id);
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (Objects.isNull(offerOptional) || Objects.isNull(candidateOptional)) {
            return "error/404";
        }
        Offer detailOffer = offerOptional.get();
        Candidate candidateOffer = candidateOptional.get();

        if (detailOffer != null && candidateOffer != null) {
            detailOffer.setStatus(Status.fromCode(Status.WAITING_FOR_RESPONSE.getCode()));
            candidateOffer.setStatus(Status.fromCode(Status.WAITING_FOR_RESPONSE.getCode()));
            offerService.save(detailOffer);
            candidateService.save(candidateOffer);
        }
        return "redirect:/offer/view/" + id;
    }

    @GetMapping("/view/cancel-offer/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'MANAGER', 'ADMIN')")
    public String cancelOffer(@PathVariable Integer id) {
        Optional<Offer> offerOptional = offerService.findById(id);
        Optional<Candidate> candidateOptional = candidateService.findById(id);
        if (Objects.isNull(offerOptional) && Objects.isNull(candidateOptional)) {
            return "error/404";
        }
        Offer detailOffer = offerOptional.get();
        detailOffer.setStatus(Status.fromCode(Status.CANCELLED_OFFER.ordinal()));

        Candidate candidateOffer = candidateOptional.get();
        candidateOffer.setStatus(Status.fromCode(Status.CANCELLED_OFFER.ordinal()));

        offerService.save(detailOffer);
        candidateService.save(candidateOffer);

        return "redirect:/offer/view/" + id;
    }
}
