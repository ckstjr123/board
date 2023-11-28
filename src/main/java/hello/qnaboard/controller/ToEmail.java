package hello.qnaboard.controller;

import hello.qnaboard.validation.EmailDomain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * 인증 요청 이메일
 */
public class ToEmail {

    @EmailDomain // @Email만으로 빈 문자열, 이메일 도메인 형식까지 검증하지 못하므로 도입
    @Email
    private String email;

    @Override
    public String toString() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
