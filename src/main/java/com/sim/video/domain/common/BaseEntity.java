package com.sim.video.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//BaseEntitiy: createAt, updateAt는 자동으로 처리(중복제거, @CreateData,@LastModifiedDate가 자동으로 적용)
@MappedSuperclass  //하위 엔터티가 물려받도록(추상화)
@EntityListeners(AuditingEntityListener.class) //JPA 생명주기(persist, update 등)을 감지하여 자동으로 동작하는 감시자
@Getter
public abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

}
