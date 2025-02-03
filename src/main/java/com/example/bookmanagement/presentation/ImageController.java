package com.example.book.controller;

import com.example.bookmanagement.domain.Image;
import com.example.bookmanagement.application.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    /**
     * 📌 도서 이미지 업로드 API
     */
    @PostMapping("/{bookId}/upload")
    public ResponseEntity<String> uploadImage(@PathVariable Long bookId, @RequestParam("file") MultipartFile file) {
        try {
            imageService.uploadImage(bookId, file);
            return ResponseEntity.ok("이미지 업로드 성공!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    /**
     * 📌 특정 도서의 이미지 목록 조회 API
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<List<Image>> getImages(@PathVariable Long bookId) {
        return ResponseEntity.ok(imageService.getImagesByBookId(bookId));
    }

    /**
     * 📌 특정 이미지 다운로드 API
     */
    @GetMapping("/download/{imageId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imageId) throws IOException {
        Image image = imageService.getImagesByBookId(imageId).get(0);
        File file = new File(image.getFilePath());
        byte[] fileContent = Files.readAllBytes(file.toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getOriginalFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    /**
     * 📌 이미지 삭제 API
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok("이미지 삭제 성공!");
    }
}

