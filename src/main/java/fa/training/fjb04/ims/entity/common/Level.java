package fa.training.fjb04.ims.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.entity.intermediateTable.JobLevel;
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
public class Level {
    @Id
    @Column(name = "level_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "level_name")
    private String name;

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    private List<JobLevel> jobLevelList = new ArrayList<>();

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    private List<Offer> offerList = new ArrayList<>();
}


