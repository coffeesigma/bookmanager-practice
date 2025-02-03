package com.example.bookmanagement.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookimages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이미지 ID

    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 저장된 파일 이름 (UUID 사용)
    private String filePath; // 저장 경로
    private Long fileSize; // 파일 크기 (KB 단위)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book; // 해당 이미지가 속한 책 (연관 관계)

    @Builder
    public Image(String originalFileName, String storedFileName, String filePath, Long fileSize, Book book) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.book = book;
    }
}

