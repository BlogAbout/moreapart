package ru.moreapart.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class UploaderService {
    private final Path UPLOAD_DIR = Paths.get("frontend/public/uploads");

    public void checkDir() throws IOException {
        if (Files.notExists(UPLOAD_DIR))
            Files.createDirectory(UPLOAD_DIR);
    }

    public String save(MultipartFile file, String prefix, Long userId) {
        String fileName = "";
        try {
            checkDir();

            String extension = file.getContentType().split("/")[1];

            String uuid = String.valueOf(UUID.randomUUID());
            String encodedString = new String(Base64.getEncoder().encode(uuid.getBytes()));
            fileName = prefix + "-" + userId + "-" + encodedString + "." + extension;
            Files.copy(file.getInputStream(), UPLOAD_DIR.resolve(fileName));
        } catch (IOException e) {
            System.out.println(e);
        }

        return fileName;
    }
}