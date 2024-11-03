package hello.qnaboard.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * 로그인 회원 정보를 담은 세션 보관용 객체
 * (implements Serializable: 레디스에 세션 데이터로 저장하기 위함)
 */
@Getter
@EqualsAndHashCode
public final class SessionMember implements Serializable {

    private final Long memberId; // 로그인 사용자 id(pk)
    private final String name; // 닉네임

    public SessionMember(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }
}