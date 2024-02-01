package fa.training.fjb04.ims.service.offer;

import fa.training.fjb04.ims.dto.offerDto.OfferDTO;
import fa.training.fjb04.ims.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfferService {

    Page<OfferDTO> getPageDate (Pageable pageable);

    List<OfferDTO> getListOffer();

  /*  Page<OfferDTO> getPageSearchDate(Pageable pageable, String serchText, String searchField, String searchStatus)*/
    Optional<Offer> findById(Integer id);

    void save (Offer offer);

    Offer saveTest(Offer offer);

    List<OfferDTO> findAllByDate(LocalDate startDate, LocalDate endDate);

    void deleteByCandidateId(Integer id);

    List<Offer> findByDueDate(LocalDate date);

}
