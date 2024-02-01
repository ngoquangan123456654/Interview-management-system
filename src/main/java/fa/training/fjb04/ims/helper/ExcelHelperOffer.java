package fa.training.fjb04.ims.helper;

import fa.training.fjb04.ims.dto.offerDto.OfferDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelperOffer {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"CandidateName", "Email", "Approver", "Department", "Notes", "Status"};
    static String SHEET = "Offers";

    public static ByteArrayInputStream OfferDTOToExcel(List<OfferDTO> offerDTOList){
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++){
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (OfferDTO offerDTO : offerDTOList){
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(offerDTO.getCandidateName());
                row.createCell(1).setCellValue(offerDTO.getEmailCandidate());
                row.createCell(2).setCellValue(offerDTO.getApprover());
                row.createCell(3).setCellValue(offerDTO.getDepartment());
                row.createCell(4).setCellValue(offerDTO.getNote());
                row.createCell(5).setCellValue(offerDTO.getStatus());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }catch (IOException e){
            throw new RuntimeException("Fail to import data to Excel file: " + e.getMessage());
        }
    }
}
