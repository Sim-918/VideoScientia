package com.sim.video.domain.review;

import com.sim.video.domain.common.BaseEntity;
import com.sim.video.domain.content.Video;
import com.sim.video.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

// 유저 1명당 1영화(드라마) 1개 리뷰
@Entity
@Table(name="review", uniqueConstraints = { //user_id+movie_id가 동시에 같은 값이 행이 존재하면 db에러를 발생시킴
        @UniqueConstraint(columnNames = {"user_id","video_id"}) //즉 유저는 하나의 영화에 대해 하나의 리뷰만 작성 가능
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //외부에서 new Review() 사용불가,빌더만 사용가능, JPA내부에서만 사용
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //리뷰 작성자
    @ManyToOne(fetch = FetchType.LAZY,optional = false)// 객체 로딩을 지연시키면 연관된 객체까지 즉시 모두 로딩됨
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    //대상 영화
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    //별점(1~10), 소수점 2자리 까지
    @Column(nullable = false, precision = 3, scale = 2)
    @DecimalMin(value = "1.00", message = "최소 평점은 1.00 이상입니다.") //소수점 2자리까지 정밀도 보장
    @DecimalMax(value = "10.00", message = "최대 평점은 10.00 이하입니다.")
    private BigDecimal rating;


    //리뷰 내용
    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    @Column(nullable = false,length = 300)
    private String content;

    //신고 횟수
    @Column(nullable = false)
    private int reportCount;

    //삭제 여부
    @Column(nullable = false)
    private boolean deleted;

    // 수정자 ID (선택)
    @Column(length = 100)
    private String modifiedBy;

    //빌더 설정
    @Builder
    public Review(User user,Video video,BigDecimal rating,String content){
        this.user=user;
        this.video=video;
        this.rating=rating;
        this.content=content;
        this.reportCount=0;
        this.deleted=false;
    }
    //신고 증가
    public void report(){
        this.reportCount++;
    }
    // 수정
    public void edit(String newContent, BigDecimal newRating, String modifierId) {
        this.content = newContent;
        this.rating = newRating;
        this.modifiedBy = modifierId;
    }
    
    //소프트 삭제
    public void softDelete() {
        this.deleted = true;
    }
}
