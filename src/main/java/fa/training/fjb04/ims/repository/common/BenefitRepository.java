package fa.training.fjb04.ims.repository.common;

import fa.training.fjb04.ims.entity.common.Benefit;
import fa.training.fjb04.ims.entity.intermediateTable.JobBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Integer> {

    @Query(value = "select b.name from Benefit b")
    List<String> findAllBenefits();

    Benefit findByName(String name);

    @Query(
            "SELECT jb FROM Benefit b " +
                    "JOIN JobBenefit jb ON b.id = jb.benefit.id " +
                    "JOIN Job j ON j.id = jb.job.id " +
                    "WHERE j.id = :id"
    )
    List<JobBenefit> getAllByJobId(Integer id);

    @Query(
            "SELECT b.name FROM Benefit b " +
                    "JOIN JobBenefit jb ON b.id = jb.benefit.id " +
                    "JOIN Job j ON j.id = jb.job.id " +
                    "WHERE j.id = :id"
    )
    List<String> getAllNameByJobId(Integer id);

    @Query("SELECT b.name FROM Benefit b")
    List<String> findAllName ();
}
