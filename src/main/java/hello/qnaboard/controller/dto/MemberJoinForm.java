package hello.qnaboard.controller.dto;

import hello.qnaboard.validation.EmailAuthCode;
import hello.qnaboard.validation.EmailDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * 회원 DTO
 */
@NoArgsConstructor
@Getter @Setter
public class MemberJoinForm {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 15, message = "닉네임은 최대 15자입니다.")
    private String name; // 닉네임

    @EmailDomain
    @Email(message = "이메일 형식을 올바르게 입력해주세요.")
    private String email; // 이메일

    @Size(min = 8, max = 16, message = "비밀번호를 8자 이상, 16자 이하로 입력해주세요.")
    private String password; // 비밀번호

    @EmailAuthCode // 인증번호 형식 검증
    private String emailAuthCode; // 회원가입 시 이메일 인증번호(Integer 타입으로 받으면 0으로 시작하는 인증번호 처리 불가)
}
