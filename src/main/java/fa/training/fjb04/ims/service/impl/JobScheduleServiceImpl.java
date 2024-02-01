package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.repository.schedule.JobScheduleRepository;
import fa.training.fjb04.ims.service.schedule.JobScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobScheduleServiceImpl implements JobScheduleService {
    private final JobScheduleRepository jobScheduleRepository;
    @Override
    public void deleteAllJobScheduleServiceById(Integer id) {
        jobScheduleRepository.deleteAllById(id);
    }
}
