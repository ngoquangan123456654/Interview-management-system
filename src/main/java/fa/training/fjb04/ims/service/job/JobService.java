package fa.training.fjb04.ims.service.job;

import fa.training.fjb04.ims.dto.job.JobDto;
import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Job findByTitle(String title);

    List<Job> getAll();

    List<String> getAllTitle();

    List<String> getAllTitleByStatus(String status) ;

    void create(Job job);

    Optional<Job> findById(Integer id);

    void update(Integer id);

    void delete(Integer id);

    Page<JobDto> searchJobList(String keyword, String status, Pageable pageable);

    List<JobDto> showAll();

    void save(JobDto jobDto, UserDetails userDetails);

    void updateJobById(Integer id, JobDto jobDto);

    void updateJobStatus();

    JobDto findJobDtoById(Integer id);
}
