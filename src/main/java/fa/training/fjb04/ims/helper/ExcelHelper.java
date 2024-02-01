package fa.training.fjb04.ims.helper;

import fa.training.fjb04.ims.dto.job.JobDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Title", "StartDate", "EndDate", "SalaryFrom", "SalaryTo", "WorkingAddress", "Description", "SkillSet", "BenefitSet", "LevelSet" };
    static String SHEET = "Jobs";

    public static boolean hasExcelFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static List<JobDto> excelToJobs(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<JobDto> jobDtos = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                JobDto jobDto = new JobDto();
                boolean check = true;

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    if (currentCell != null && currentCell.getCellType() != CellType.BLANK) {
                        // Cell is not empty
                        switch (cellIdx) {
                            case 0 -> jobDto.setTitle(currentCell.getStringCellValue());
                            case 1 -> jobDto.setStartDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            case 2 -> jobDto.setEndDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            case 3 -> jobDto.setSalaryFrom(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                            case 4 -> jobDto.setSalaryTo(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                            case 5 -> jobDto.setWorkingAddress(currentCell.getStringCellValue());
                            case 6 -> jobDto.setDescription(currentCell.getStringCellValue());
                            case 7 -> jobDto.setSkill(Arrays.asList(currentCell.getStringCellValue().split(",\\s*")));
                            case 8 -> jobDto.setBenefits(Arrays.asList(currentCell.getStringCellValue().split(",\\s*")));
                            case 9 -> jobDto.setLevel(Arrays.asList(currentCell.getStringCellValue().split(",\\s*")));
                            default -> {
                            }
                        }

                        cellIdx++;
                    } else {
                        check = false;
                        break;
                    }
                }

                if (!check) {
                    break;
                }

                jobDtos.add(jobDto);
            }

            workbook.close();

            return jobDtos;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
