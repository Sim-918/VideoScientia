package com.sim.video.domain.user;

import com.sim.video.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


//todo 엔터티에서 값을 바로 대입하지 않고 생성자에 선언을 해야함
@Entity
@Table(name="user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생성자를 자동으로 만들고 속성(access = AccessLevel.PROTECTED)을 추가하면서 외부에서 이 생성자를 쓰지 말라고 유도함
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false,unique = true,length = 20)
    private String userId;

    @Column(nullable = false,length = 50)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 1)
    private Yn isDel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 1)
    private Yn isLock;

    @Column
    private int pwFailCnt;

    private Integer age;

    @Email
    @Column(length = 30)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gen;

    @Column(length = 13)
    private String phoneNum;

    @Builder
    public User(String userId, String password, Role role, Integer age, String email, Gender gen, String phoneNum) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.age = age;
        this.email = email;
        this.gen = gen;
        this.phoneNum = phoneNum;
        this.isDel = Yn.N;
        this.isLock = Yn.N;
        this.pwFailCnt = 0;
    }
    //비밀번호 변경 도메인 메서드(setter막기)
    public void changePassword(String encodedPassword){
        this.password=encodedPassword;
    }
    public void increaseFailCount(){
        this.pwFailCnt++;
    }
    //비밀번호 실패 횟수 초기화 도메인 메서드
    public void resetFailCount(){
        this.pwFailCnt=0;
    }
    public void unlockAccount() {
        this.isLock = Yn.N;
    }

    public enum Gender {
        M, F
    }
    //spring security가 요구하는 권한 ROLE_ 가 prefix이다
    public enum Role {
        ROLE_ADMIN, ROLE_USER
    }

    public enum Yn {
        Y, N
    }
}
