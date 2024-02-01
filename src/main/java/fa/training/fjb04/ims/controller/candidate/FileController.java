package fa.training.fjb04.ims.controller.candidate;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {
    private Path foundFile;

    @GetMapping("files/{folder}/{filename}")
    public ResponseEntity<?> getFile(@PathVariable("filename") String filename) throws IOException {

        Path dirPath = Paths.get("uploads/file");
        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().equals(filename)) {
                foundFile = file;
                return;
            }
        });
        Resource resource = null;

        try {
            resource = new UrlResource(foundFile.toUri());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
