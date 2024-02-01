package fa.training.fjb04.ims.service.schedule;

import fa.training.fjb04.ims.dto.schedule.ScheduleDto;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.entity.Schedule;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.enums.StatusSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ScheduleService {

    void deleteAllByJobId(Integer jobId);

    public void updateSchedule(ScheduleDto scheduleDto);

    void save(ScheduleDto scheduleDto);

    Schedule findById(Integer id);

    Page<ScheduleDto> searchScheduleList(String keyword, String interviewer, StatusSchedule statusSchedule, Pageable pageable);
    User findMainInterviewByScheduleId(Integer id);

    void deleteById(Integer id);

    void sendEmail(String recipientEmail, Offer offer, String link);
}
