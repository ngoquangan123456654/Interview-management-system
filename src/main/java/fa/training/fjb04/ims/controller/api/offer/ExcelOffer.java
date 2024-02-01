package fa.training.fjb04.ims.controller.api.offer;

import fa.training.fjb04.ims.service.excel.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/excel")
public class ExcelOffer {

    @Autowired
    ExcelService excelService;

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile(){
        String fileName = "offerList.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.load());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnc.ms-excel"))
                .body(file);
    }

    //dowload by date
    @PostMapping("/download/contractPeriod")
    public ResponseEntity<Resource> getFileByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ){
//        String fileName = "offerListByDate.xlsx";
        String fileName = String.format("Offerlist-%s_%s.xlsx", startDate, endDate);

        InputStreamResource file = new InputStreamResource(excelService.loadByDate(startDate, endDate));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnc.ms-excel"))
                .body(file);
    }
}
