package fa.training.fjb04.ims.scheduler;


import fa.training.fjb04.ims.dto.request.MailReminderRequest;
import fa.training.fjb04.ims.entity.Candidate;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.entity.User;
import fa.training.fjb04.ims.service.offer.OfferService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class ReminderOffer {

    private final OfferService offerService;
    private final ScheduleService scheduleService;
    @Scheduled(cron = " 0 0 8 * * *")
    public void sendEmailReminderAuto(){

        try{
            LocalDate dueDate = LocalDate.now();
            List<Offer> offerList = offerService.findByDueDate(dueDate);

            for (Offer offer : offerList) {
                Candidate candidate = offer.getCandidate();
                String email = candidate.getEmail();
                String link = "http://localhost:8080/offer/view/" + offer.getId();
                scheduleService.sendEmail(email, offer, link);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
