package com.BDiary.webDemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TeamNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;        // 구단명
    private String title;           // 뉴스 제목
    @Column(columnDefinition = "TEXT")
    private String imageUrl;        // 이미지 URL
    private String newsUrl;         // 원본 뉴스 URL
    private LocalDateTime createdAt; // 작성일
    private String category;        // 뉴스 카테고리 (구단소식, 팀소식 등)
}