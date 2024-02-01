package fa.training.fjb04.ims.scheduler;

import fa.training.fjb04.ims.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateJobStatus {
    private final JobService jobService;
@Scheduled(fixedDelay = 30000)
    public void updateJobStatus(){
        jobService.updateJobStatus();
    }
}
