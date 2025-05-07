package com.sim.video.domain.like;

import com.sim.video.domain.common.BaseEntity;
import com.sim.video.domain.content.Video;
import com.sim.video.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "video_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //좋아요 누른 유저
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    //좋아요 대상
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

}
