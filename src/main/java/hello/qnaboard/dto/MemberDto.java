package hello.qnaboard.dto;

import hello.qnaboard.constant.Role;
import hello.qnaboard.domain.Member;
import hello.qnaboard.validation.EmailAuthCode;
import hello.qnaboard.validation.EmailDomain;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 회원 DTO
 */
@Getter @Setter
public class MemberDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 15, message = "닉네임은 최대 15자까지 가능합니다.")
    private String name; // 닉네임

    @EmailDomain // @Email만으로 빈 문자열, 이메일 도메인 형식까지 검증하지 못하므로 도입
    @Email(message = "이메일 형식을 올바르게 입력해주세요.")
    private String email; // 이메일

    @Size(min = 8, max = 16, message = "비밀번호를 8자 이상, 16자 이하로 입력해주세요.")
    private String password; // 비밀번호

    @EmailAuthCode // 인증번호 형식 검증
    private String emailAuthCode; // 회원가입 시 이메일 인증번호(Integer 타입으로 받으면 0으로 시작하는 인증번호 처리 불가)

    //회원 정보 수정 시
    private String modifiedBy;
    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper(); // Member → MemberDto 변환 시 사용

    public MemberDto() {}

    public MemberDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static MemberDto of(Member member) {
        return modelMapper.map(member, MemberDto.class); // Member → MemberDto
    }

}
