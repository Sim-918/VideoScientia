package com.sim.video.domain.content;

// TMDB API 기반 영화/드라마 정보 저장
// 좋아요, 즐겨찾기, 리뷰와 연관
// 영화/TV 타입 구분

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="video",uniqueConstraints = {
        @UniqueConstraint(columnNames = "tmdbId")
})
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //tmdb의 고유 id
    @Column(nullable = false)
    private Long tmdbId;

    //제목
    @Column(nullable = false, length = 255)
    private String title;

    // 포스터
    @Column(length = 500)
    private String posterPath;

    //줄거리
    @Column(columnDefinition = "TEXT")
    private String overview;

    // tmdb 영화/드라마 개봉일자
    @Column
    private LocalDate releaseDate; //tmdb api의 release_date가 yyyy-mm-dd타입

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 10)
    private VideoType type;

    public enum VideoType{
        MOVIE,TV
    }
}
