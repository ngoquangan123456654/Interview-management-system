package fa.training.fjb04.ims.controller.job;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/download")
public class DownloadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    @GetMapping("/{filename}")
    public HttpEntity<ByteArrayResource> createExcelWithTaskConfiguration(@PathVariable("filename") String fileName) throws IOException {

        String filePath = "/static/files/" + fileName;
        InputStream inputStream = getClass().getResourceAsStream(filePath);

        if (inputStream == null) {
            LOGGER.error("File not found: {}", filePath);

            return null;
        }

        byte[] excelContent;
        try {
            excelContent = IOUtils.toByteArray(inputStream);
        } finally {
            inputStream.close();
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return new HttpEntity<>(new ByteArrayResource(excelContent), header);
    }
}
