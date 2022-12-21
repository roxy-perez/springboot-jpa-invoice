package com.spring.datajpa.app.springbootdatajpa.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final static String UPLOADS_FOLDER = "uploads";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public UrlResource load(String filename) throws MalformedURLException {

        Path pathImage = getPath(filename);

        log.info("pathImage: " + getPath(filename));

        UrlResource resource = null;
        resource = new UrlResource(pathImage.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Error: no se puede cargar la imagen: " + pathImage.toString());
        }
        return resource;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rootPath = getPath(uniqueFileName);

        log.info("rootpath: " + rootPath);

        Files.copy(file.getInputStream(), rootPath);

        return uniqueFileName;
    }

    @Override
    public boolean delete(String filename) {
        Path rootPath = getPath(filename);
            File file = rootPath.toFile();

            if (file.exists() && file.canRead()) {
                if (file.delete()) { // Si se eliminó la foto
                   return true;
                }
            }
        return false;
    }

    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
        
    }

    @Override
    public void init() throws IOException {
        Files.createDirectory(Paths.get(UPLOADS_FOLDER));
        
    }

    
}
