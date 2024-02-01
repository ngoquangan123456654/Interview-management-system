package fa.training.fjb04.ims.controller.api.offer;

import fa.training.fjb04.ims.dto.offerDto.OfferDTO;
import fa.training.fjb04.ims.dto.offerDto.OfferSearchDTO;
import fa.training.fjb04.ims.enums.Department;
import fa.training.fjb04.ims.enums.Status;
import fa.training.fjb04.ims.repository.offer.OfferRepository;
import fa.training.fjb04.ims.service.offer.OfferService;
import fa.training.fjb04.ims.service.schedule.ScheduleService;
import fa.training.fjb04.ims.service.schedule.UserScheduleSevice;
import fa.training.fjb04.ims.util.Common;
import jakarta.servlet.ServletOutputStream;
import lombok.AllArgsConstructor;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offer")
@AllArgsConstructor
public class OfferApi {

    private final OfferService offerService;


    private final UserScheduleSevice userScheduleService;

    private final ScheduleService scheduleService;

    private final OfferRepository offerRepository;
    private final Common common;

    @GetMapping
    public ResponseEntity<?> getListOffers(@RequestParam(required = false, defaultValue = "0") int pageIndex,
                                           @RequestParam(required = false, defaultValue = "2") int pageSize,
                                           Model model,
                                           Sort sort){
        List<Status> statusOffer = Arrays.asList(
                Status.WAITING_FOR_APPROVAL,
                Status.APPROVED_OFFER,
                Status.REJECTED_OFFER,
                Status.WAITING_FOR_RESPONSE,
                Status.ACCEPTED_OFFER,
                Status.DECLINED_OFFER,
                Status.CANCELLED_OFFER
        );
        model.addAttribute("statusList", statusOffer);

        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, sort);

        Page<OfferDTO> offerDTOPage = offerService.getPageDate(pageRequest);
        return ResponseEntity.ok(offerDTOPage);
    }


    @GetMapping("/v1")
    public ResponseEntity<Page<OfferSearchDTO>> getUserList(
            @RequestParam(defaultValue = "0", required = false, value = "pageIndex") int pageIndex,
            @RequestParam(defaultValue = "2", required = false, value = "pageSize") int pageSize,
            @RequestParam(required = false, value = "keyword") String keyword,
            @RequestParam(required = false, value = "department") String department,
            @RequestParam(required = false, value = "status") String status) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Status enumStatus = (!common.isBlankOrEmpty(status)) ? Status.valueOf(status) : null;

        Department department1 = (!common.isBlankOrEmpty(department)) ? Department.valueOf(department) : null;
        Page<OfferSearchDTO> userDtoPage = offerRepository.getList(keyword, department1,enumStatus, pageable);
        return new ResponseEntity<>(userDtoPage, HttpStatus.OK);
    }
}
