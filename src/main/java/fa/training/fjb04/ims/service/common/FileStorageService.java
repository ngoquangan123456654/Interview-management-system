package fa.training.fjb04.ims.service.common;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    public void init();

    public String save(MultipartFile file, String folder) throws IOException;

    public Resource load(String filename);

    public boolean delete(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

    public Resource loadFileAsResource(String relativePath) throws MalformedURLException, FileNotFoundException;

    public Resource downloadFile(String fileUrl) throws IOException;

    String getFileLocation();
}
