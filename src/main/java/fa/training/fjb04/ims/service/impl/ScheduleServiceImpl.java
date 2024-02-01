package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.dto.schedule.ScheduleDto;
import fa.training.fjb04.ims.entity.*;
import fa.training.fjb04.ims.entity.intermediateTable.UserSchedule;
import fa.training.fjb04.ims.enums.Result;
import fa.training.fjb04.ims.enums.StatusSchedule;
import fa.training.fjb04.ims.repository.job.JobRepository;
import fa.training.fjb04.ims.repository.schedule.ScheduleRepository;
import fa.training.fjb04.ims.repository.user.UserRepository;
import fa.training.fjb04.ims.repository.user.UserScheduleRepository;
import fa.training.fjb04.ims.service.candidate.CandidateService;
import fa.training.fjb04.ims.service.common.FileStorageService;
import fa.training.fjb04.ims.service.job.JobService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final CandidateService candidateService;
    private final JobService jobService;
    private final FileStorageService fileStorageService;

    private final JavaMailSender javaMailSender;

    @Override
    public Schedule findById(Integer id) {
        return scheduleRepository.findByScheduleId(id);
    }

    ;

    @Override
    public void save(ScheduleDto scheduleDto) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDto, schedule);

        schedule.setCreatedDate(LocalDateTime.now());
        schedule.setLastModifiedDate(LocalDateTime.now());
        schedule.setCreatedBy("Dat Dep Zai");
        schedule.setLastModifiedBy("Dat Dep Zai");

        User recruiter = userRepository.findByUserName(scheduleDto.getRecruiter());
        User mainInterviewer = userRepository.findByUserName(scheduleDto.getMainInterviewer());
        Candidate candidate = scheduleDto.getCandidate();
        Job job = jobService.findByTitle(scheduleDto.getJob());

        schedule.setRecruiter(recruiter);
        schedule.setMainInterviewer(mainInterviewer);
        schedule.setCandidate(candidate);
        schedule.setJob(job);
        schedule.setResult(Result.NA);
        schedule.setStatusSchedule(StatusSchedule.NEW);

        scheduleRepository.save(schedule);

        for (String interviewerName : scheduleDto.getInterviewerList()) {
            User user = userRepository.findByUserName(interviewerName);
            UserSchedule userSchedule = new UserSchedule();
            userSchedule.setSchedule(schedule);
            userSchedule.setUser(user);
            userScheduleRepository.save(userSchedule);
        }
    }

    @Override
    @Transactional
    public void updateSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleDto.getScheduleId());
        BeanUtils.copyProperties(scheduleDto, schedule);
        schedule.setMainInterviewer(userRepository.findByUserName(scheduleDto.getMainInterviewer()));
        schedule.setRecruiter(userRepository.findByUserName(scheduleDto.getRecruiter()));
        schedule.setJob(jobRepository.findByTitle(scheduleDto.getJob()));
//        schedule.setCandidate(candidateRepository.findByCandidateEmail(scheduleDto.getCandidate()));
        if (userScheduleRepository.existsBySchedule(schedule)) {
            userScheduleRepository.deleteByScheduleId(schedule.getId());
        }
        for (String userValue : scheduleDto.getInterviewerList()) {
            User user = userRepository.findByUserName(userValue);
            UserSchedule userSchedule = new UserSchedule();
            userSchedule.setSchedule(schedule);
            userSchedule.setUser(user);
            userScheduleRepository.save(userSchedule);
        }
        scheduleRepository.save(schedule);
    }

    @Override
    public Page<ScheduleDto> searchScheduleList(String keyword, String interviewer, StatusSchedule statusSchedule, Pageable pageable) {

        Page<Schedule> schedulePage = scheduleRepository.searchScheduleList(keyword, interviewer, statusSchedule, pageable);
        return schedulePage.map(sch -> new ScheduleDto(sch.getId(), sch.getTitle()
                , sch.getCandidate()
                , sch.getMainInterviewer() != null ? sch.getMainInterviewer().getFullName() : "null"
                , userRepository.getAllNameByScheduleId(sch.getId())
                , sch.getScheduleDate()
                , sch.getScheduleTimeFrom()
                , sch.getScheduleTimeTo()
                , sch.getResult().getName()
                , sch.getStatusSchedule().getName()
                , sch.getJob().getTitle()));
    }

    @Override
    public void deleteAllByJobId(Integer jobId) {
        scheduleRepository.deleteByJobId(jobId);
    }

    @Override
    public User findMainInterviewByScheduleId(Integer id) {
        return scheduleRepository.findMainInterviewByScheduleId(id);
    }

    @Override
    public void deleteById(Integer id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public void sendEmail(String recipientEmail, Offer offer, String link) {

        String dueDate = offer.getDueDate().toString();
        String interviewTitle = offer.getSchedule().getTitle();
        String candidateName = offer.getCandidate().getFullName();
        String candidatePosition = offer.getCandidate().getPosition().getName();
        String recruiterAccount = offer.getRecruiter().getUserName();
        String fileCvName = offer.getCandidate().getCvAttachment();

       /* File file = new File(fileStorageService.getFileLocation() + "/" + fileCvName);

        FileSystemResource fileAttach = new FileSystemResource(file);*/

        String subject = "no-reply-email-IMS-system " + interviewTitle;
        String content = "<p>This email is from IMS system,</p>" +
                "<p>You have an offer to take action For Candidate " + candidateName + "</p>" +
                "<p>position " + candidatePosition + " before " + dueDate + ", the contract is</p>" +
                "<p>attached with this no-reply-email</p>" +
                "<p>Please refer this link to take action " + link + "</p>" +
                "<p>If anything wrong, please reach-out recruiter " + recruiterAccount + "</p>" +
                "<p> We are so sorry for this inconvenience.</p>" +
                "<p>Thanks & Regards!</p>" +
                "<p>IMS Team.</p>";

        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(recipientEmail);
            message.setFrom("fjb04@fa.com", "FA Support");
            message.setSubject(subject);
            message.setText(content, true);
//            message.addAttachment("file CV", fileAttach);
        };

        try {
            javaMailSender.send(preparator);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }


}
