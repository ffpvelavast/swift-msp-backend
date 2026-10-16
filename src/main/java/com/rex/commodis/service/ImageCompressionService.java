/** Service handling ImageCompression operations; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rex.commodis.dto.ImageCompressionResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class ImageCompressionService {

    @Value("${app.image.storage-path:uploads}")
    private String storagePath;

    /**
     * Compresses the given image file and saves to disk.
     * @param file Uploaded MultipartFile
     * @param maxWidth Max width in pixels
     * @param maxHeight Max height in pixels
     * @param quality Compression quality (0.0–1.0)
     * @return DTO with compression metadata
     */
    public ImageCompressionResponse compressAndSave(MultipartFile file, int maxWidth, int maxHeight, float quality) {
        try {
            // Ensure storage directory exists
            Path storageDir = Paths.get(storagePath);
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            // Original metadata
            String originalName = file.getOriginalFilename();
            long originalSize = file.getSize();

            // Generate unique file name
            String ext = getExtension(originalName);
            String compressedName = UUID.randomUUID().toString() + ext;
            Path compressedPath = storageDir.resolve(compressedName);

            // Perform compression
            Thumbnails.of(file.getInputStream())
                      .size(maxWidth, maxHeight)
                      .outputQuality(quality)
                      .toFile(compressedPath.toFile());

            long compressedSize = Files.size(compressedPath);
            // Build accessible URL (assuming /uploads/** is mapped)
            String url = "/" + storagePath + "/" + compressedName;

            return ImageCompressionResponse.builder()
                    .originalFileName(originalName)
                    .originalSize(originalSize)
                    .compressedFileName(compressedName)
                    .compressedSize(compressedSize)
                    .url(url)
                    .build();
        } catch (IOException e) {
            log.error("Error compressing image", e);
            throw new RuntimeException("Image compression failed.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
