package fa.training.fjb04.ims.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.intermediateTable.JobBenefit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Benefit {
    @Id
    @Column(name = "benefit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "benefit_name")
    private String name;

    @OneToMany(mappedBy = "benefit")
    @JsonIgnore
    private List<JobBenefit> jobBenefitList = new ArrayList<>();

    @Override
    public String toString() {
        return "Benefit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
