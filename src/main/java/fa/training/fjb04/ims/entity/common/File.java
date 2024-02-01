package fa.training.fjb04.ims.entity.common;

import fa.training.fjb04.ims.entity.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File{
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_name")
    private String name;

    @Column(name = "file_url")
    private String url;

}
