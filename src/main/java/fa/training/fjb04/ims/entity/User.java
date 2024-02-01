package fa.training.fjb04.ims.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.training.fjb04.ims.entity.common.Roles;
import fa.training.fjb04.ims.entity.intermediateTable.UserSchedule;
import fa.training.fjb04.ims.enums.*;
import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Gender;
import fa.training.fjb04.ims.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Table
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractAuditingEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "FullName can not be empty")
    private String fullName;

    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "UserName can not be empty")
    private String userName;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password can not be empty")
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull(message = "DateOfBirth can not be empty")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", nullable = false)
    @NotBlank(message = "PhoneNumber can not be empty")
    private String phoneNumber;

    @Column(name = "status")
    @NotNull(message = "Status can not be empty")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email can not be empty")
    private String email;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Address can not be empty")
    private String address;

    @Column(name = "gender")
    @NotNull(message = "Gender can not be empty")
    private Gender gender;

    @Column(name = "department")
    @NotNull(message = "Department can not be empty")
    private Department department;

    @Column(name = "note")
    private String note;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "time_token")
    private LocalDateTime timeToken;

    @Column(name = "link_gmai_has_used")
    private Integer linkGmailHasUsed;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Candidate> candidateList = new ArrayList<>();

    @OneToMany(mappedBy = "recruiter")
    private List<Offer> recruiterOfferList = new ArrayList<>();

    @OneToMany(mappedBy = "approver")
    @JsonIgnore
    private List<Offer> approverOfferList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserSchedule> userScheduleList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    @JsonIgnore
    private Set<Roles> roles= new HashSet<>();

    @OneToMany(mappedBy = "recruiter")
    @JsonIgnore
    private List<Schedule> schedulesRecruiter = new ArrayList<>();

    @OneToMany(mappedBy = "mainInterviewer")
    @JsonIgnore
    private List<Schedule> schedulesMItvw = new ArrayList<>();


}