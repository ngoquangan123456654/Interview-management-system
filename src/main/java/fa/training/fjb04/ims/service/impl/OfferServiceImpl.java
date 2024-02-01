package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.dto.offerDto.OfferDTO;
import fa.training.fjb04.ims.entity.Offer;
import fa.training.fjb04.ims.repository.candidate.CandidateRepository;
import fa.training.fjb04.ims.repository.offer.OfferRepository;
import fa.training.fjb04.ims.service.offer.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final CandidateRepository candidateRepository;

    @Override
    public Page<OfferDTO> getPageDate(Pageable pageable) {
        Page<Offer> offerPage = offerRepository.findAll(pageable);
        return offerPage.map(convert -> new OfferDTO(convert.getId(), convert.getCandidate().getFullName(), convert.getCandidate().getEmail(),
            convert.getApprover().getFullName(), convert.getDepartment().name(), convert.getNote(), convert.getStatus().getName()));
    }

    //TODO: Dowload File:
    @Override
    public List<OfferDTO> getListOffer() {
        List<Offer> offerList = offerRepository.findAll();
        return offerList.stream().map(old -> new OfferDTO(old.getId(), old.getCandidate().getFullName(), old.getCandidate().getEmail(),
                old.getApprover().getFullName(), old.getDepartment().name(), old.getNote(), old.getStatus().name()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferDTO> findAllByDate(LocalDate startDate, LocalDate endDate) {
        List<Offer> offerListByDate = offerRepository.findByContractPeriodFromBetween(startDate, endDate);
        return offerListByDate.stream().map(old -> new OfferDTO(old.getId(), old.getCandidate().getFullName(), old.getCandidate().getEmail(),
                        old.getApprover().getFullName(), old.getDepartment().name(), old.getNote(), old.getStatus().name()))
                .collect(Collectors.toList());
    }

    private OfferDTO convertToOfferDTO(Offer offer) {
        return new OfferDTO(offer.getId(), offer.getCandidate().getFullName(), offer.getCandidate().getEmail(),
                offer.getApprover().getFullName(), offer.getDepartment().getName(), offer.getNote(), offer.getStatus().getName());
    }

    @Override
    public Optional<Offer> findById(Integer id) {
        return offerRepository.findById(id);
    }

    @Override
    public void save(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    public Offer saveTest(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public void deleteByCandidateId(Integer id) {
        offerRepository.deleteById(id);
    }

    @Override
    public List<Offer> findByDueDate(LocalDate date) {
        return offerRepository.findByDueDate(date);
    }
}
