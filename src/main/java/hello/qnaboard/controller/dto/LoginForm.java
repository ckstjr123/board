package hello.qnaboard.controller.dto;

import hello.qnaboard.validation.EmailDomain;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/** 로그인 폼 */
@Getter
@Setter
public class LoginForm {

    @EmailDomain
    @Email
    private String email;

    @Size(min = 8, max = 16)
    private String password;
}