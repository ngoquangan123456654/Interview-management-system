package fa.training.fjb04.ims.entity.intermediateTable;

import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.entity.common.Level;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import fa.training.fjb04.ims.entity.Job;
import fa.training.fjb04.ims.entity.common.Level;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobLevel {
    @Id
    @Column(name = "job_level_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @JsonBackReference
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    @JsonBackReference
    private Level level;
}

