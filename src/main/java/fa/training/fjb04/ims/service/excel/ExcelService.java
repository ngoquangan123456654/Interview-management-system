package fa.training.fjb04.ims.service.excel;
import fa.training.fjb04.ims.dto.offerDto.OfferDTO;
import fa.training.fjb04.ims.helper.ExcelHelperOffer;
import fa.training.fjb04.ims.service.offer.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelService {

    private final OfferService offerService;
    public ByteArrayInputStream load(){

        //TODO:Check Here:
        List<OfferDTO> offerDTOList = offerService.getListOffer();
        ByteArrayInputStream in = ExcelHelperOffer.OfferDTOToExcel(offerDTOList);
        return in;
    }

    public ByteArrayInputStream loadByDate(LocalDate startDate, LocalDate endDate){

        //TODO:Check Here:
        List<OfferDTO> offerDTOList = offerService.findAllByDate(startDate, endDate);
        ByteArrayInputStream in = ExcelHelperOffer.OfferDTOToExcel(offerDTOList);
        return in;
    }
}
