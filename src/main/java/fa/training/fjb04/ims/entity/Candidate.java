package fa.training.fjb04.ims.entity;

import fa.training.fjb04.ims.entity.common.File;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.common.HighLevel;
import fa.training.fjb04.ims.entity.intermediateTable.CandidateSkill;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.Position;
import fa.training.fjb04.ims.enums.Status;
import fa.training.fjb04.ims.util.convert.GenderConverter;
import fa.training.fjb04.ims.util.convert.PositionConverter;
import fa.training.fjb04.ims.util.convert.StatusConverter;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidate extends AbstractAuditingEntity {
    @Id
    @Column(name = "candidate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candidateId;

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "FullName can not be empty")
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email can not be empty")
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    @NotBlank(message = "PhoneNumber can not be empty")
    private String phoneNumber;

    @Column(name = "gender",nullable = false)
    @Convert(converter = GenderConverter.class)
    @NotNull(message = "Gender can not be empty")
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "DateOfBirth can not be empty")
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Address can not be empty")
    private String address;

    @Column(name = "current_position", nullable = false)
    @Convert(converter = PositionConverter.class)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "status", nullable = false)
    @Convert(converter = StatusConverter.class)
    @NotNull(message = "Status can not be empty")
    private Status status;

    @Column(name = "cv_attachment")
    private String cvAttachment;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "year_of_experience")
    private Integer yearOfExperience;

    @ManyToOne
    @JoinColumn(name = "high_level_id",nullable = false)
    @NotNull(message = "HighLevel can not be empty")
    @JsonIgnore
    private HighLevel highLevel;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @NotNull(message = "FullName can not be empty")
    @JsonIgnore
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private File file;

    @OneToMany(mappedBy = "candidate")
    @JsonIgnore
    private List<CandidateSkill> candidateSkillList = new ArrayList<>();

    @OneToOne(mappedBy = "candidate")
    @JsonIgnore
    private Offer offer;

    @OneToOne(mappedBy = "candidate")
    @JsonIgnore
    private Schedule schedule;

    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId=" + candidateId +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", position=" + position +
                ", status=" + status +
                ", note='" + note + '\'' +
                ", yearOfExperience=" + yearOfExperience +
                '}';
    }
}
