package fa.training.fjb04.ims.entity.intermediateTable;

import fa.training.fjb04.ims.entity.common.Benefit;
import fa.training.fjb04.ims.entity.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBenefit {
    @Id
    @Column(name = "job_benefit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;
}

