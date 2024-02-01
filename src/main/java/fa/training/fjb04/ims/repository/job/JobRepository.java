package fa.training.fjb04.ims.repository.job;

import fa.training.fjb04.ims.entity.Job;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    @Query(
            "SELECT j FROM Job j " +
                    "WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "           CAST(j.startDate AS STRING) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "           CAST(j.endDate AS STRING) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                    "    AND (:status IS NULL OR j.status = :status) " +
                    "ORDER BY (CASE j.status " +
                    "WHEN 'Open' THEN 1 " +
                    "WHEN 'Draft' THEN 2 " +
                    "WHEN 'Closed' THEN 3 " +
                    "END) ASC, j.startDate ASC"
    )
    Page<Job> getSearchList(String keyword, String status, Pageable pageable);

//    @Query(
//            "SELECT j FROM Job j " +
//                    "LEFT JOIN JobSkill jb ON j.id = jb.job.id " +
//                    "LEFT JOIN JobLevel jl ON j.id = jl.job.id " +
//                    "WHERE (:keyword IS NULL OR " +
//                    "           LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//                    "           LOWER(jb.skills.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//                    "           CAST(j.startDate AS STRING) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//                    "           CAST(j.endDate AS STRING) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//                    "           LOWER(jl.level.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//                    "    AND (:status IS NULL OR j.status = :status)" +
//                    "ORDER BY (CASE j.status " +
//                    "WHEN 'Open' THEN 1 " +
//                    "WHEN 'Draft' THEN 2 " +
//                    "WHEN 'Closed' THEN 3 " +
//                    "END) ASC, j.startDate ASC"
//    )
//    Page<Job> getSearchList(String keyword, String status, Pageable pageable);

    @Query(value = "select j from Job j")
    List<Job> findAllJob();

    @Query(value = "select j.title FROM Job j")
    List<String> findAllJobTitle();

    @Query(value = "select j.title FROM Job j WHERE j.status = :status")
    List<String> findAllJobTitleByStatus(String status);

    @Query(value = "select j from Job j where j.title = :title")
    Job findByTitle(@NotBlank(message = "title can not be empty") String title);



}
