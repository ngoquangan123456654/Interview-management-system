package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.service.common.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class FileLocalStorageService implements FileStorageService {

    private final Path root = Paths.get("./uploads");
    private Path foundFile;

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file, String folder) throws IOException {
        Path relativePath = getRelativePath(folder, file.getOriginalFilename());

        createFolder(folder);

        file.transferTo(root.resolve(relativePath));

        return relativePath.toString();

    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
    @Override
    public Resource loadFileAsResource(String relativePath) throws MalformedURLException, FileNotFoundException {
        Path absolute = root.resolve(relativePath);
        Resource resource = new UrlResource(absolute.toUri());
        if(resource.exists()){
            return resource;
        }
        throw new FileNotFoundException("Can not find the file with url: " + relativePath);
    }

    @Override
    public Resource downloadFile(String relativePath) throws IOException {
        Path absolute = root.resolve(relativePath);

        Files.list(absolute).forEach(file -> {
            if (file.getFileName().toString().endsWith(relativePath)) {
                foundFile = file;
                return;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }

    @Override
    public String getFileLocation() {
        if (foundFile != null){
            return foundFile.toAbsolutePath().toString();
        }
        else {
            return "File not found or set.";
        }
//        return root;
    }


    //create Folder:
    private void createFolder(String folder) throws IOException {
        if(!StringUtils.hasText(folder)){
            return;
        }
        Path folderPath = root.resolve(folder);

        if(Files.notExists(folderPath)){
            Files.createDirectories(folderPath);
        }
    }
    private Path getRelativePath(String folder, String fileName){
        Objects.requireNonNull(fileName);

        return StringUtils.hasText(folder) ? Paths.get(folder).resolve(fileName) : Paths.get(folder);
    }

}
