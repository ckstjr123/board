package hello.qnaboard.repository.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 회원 수정용 DTO
 */
@NoArgsConstructor
@Getter @Setter
public class MemberUpdateDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 15, message = "닉네임은 최대 15자입니다.")
    private String name; // 닉네임

    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password; // 비밀번호

    private LocalDateTime updateTime; //this.setUpdateTime(LocalDateTime.now());
}
