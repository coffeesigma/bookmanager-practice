package com.example.bookmanagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByBookId(Long bookId); // 특정 도서의 이미지 조회
}
