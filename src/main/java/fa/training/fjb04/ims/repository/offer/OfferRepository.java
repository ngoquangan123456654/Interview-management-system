package fa.training.fjb04.ims.repository.offer;

import fa.training.fjb04.ims.dto.offerDto.OfferSearchDTO;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    List<Offer> findByContractPeriodFromBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
              SELECT new fa.training.fjb04.ims.dto.offerDto.OfferSearchDTO(o.id,c.fullName, c.email, u.fullName, o.department, o.note,o.status)
              FROM Offer o
              INNER JOIN o.schedule s
              INNER JOIN o.candidate c
              INNER JOIN o.approver u
              
              WHERE ((:keyword Is null or c.email LIKE %:keyword%)
               OR (:keyword Is null or c.fullName LIKE %:keyword%)
              OR (:keyword Is null or o.note LIKE %:keyword%)
              OR (:keyword Is null or o.approver.fullName LIKE %:keyword%))
            
            AND(:field is null or o.department = :field)
             AND (:status is null or o.status = :status)
              """)
    Page<OfferSearchDTO> getList(String keyword, Department field, Status status, Pageable pageable);

    List<Offer> findByDueDate(LocalDate duedate);
}
