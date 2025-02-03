package com.example.bookmanagement.application;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.domain.BookRepository;
import com.example.bookmanagement.domain.Image;
import com.example.bookmanagement.domain.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final BookRepository bookRepository;

    private final String uploadDir = "/Users/your-username/uploads"; // 업로드 폴더 경로

    /**
     * 📌 도서 이미지 업로드
     */
    public void uploadImage(Long bookId, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        // 저장할 파일 이름 생성 (UUID 사용)
        String originalFilename = file.getOriginalFilename();
        String storedFilename = UUID.randomUUID() + "_" + originalFilename;
        String filePath = uploadDir + "/" + storedFilename;

        // 파일 저장
        file.transferTo(new File(filePath));

        // 이미지 엔티티 저장
        Image image = Image.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFilename)
                .filePath(filePath)
                .fileSize(file.getSize())
                .book(book)
                .build();

        imageRepository.save(image);
    }

    /**
     * 📌 특정 도서의 이미지 조회
     */
    public List<Image> getImagesByBookId(Long bookId) {
        return imageRepository.findByBookId(bookId);
    }

    /**
     * 📌 이미지 삭제
     */
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다."));

        // 파일 삭제
        File file = new File(image.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // DB에서 삭제
        imageRepository.delete(image);
    }
}

