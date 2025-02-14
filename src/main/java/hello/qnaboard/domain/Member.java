package hello.qnaboard.domain;

import hello.qnaboard.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 사용자 / 관리자
 */
@Getter
@NoArgsConstructor
@ToString
public class Member {

    private Long id; // member_id

    private String name; // 닉네임(UNIQUE)

    private String email; // 이메일(UNIQUE)

    private String password;

    private Role role;

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    /**
     * Member 생성 메서드
     * @param name
     * @param email
     * @param password
     * @return Member
     */
    public static Member createMember(String name, String email, String password) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = password;
        member.role = Role.USER;

        // 년-월-일 시:분:초:fractional seconds(timestamp(6)으로 지정했으므로 MySQL DB에는 yyyy-MM-dd HH:mm:ss:SSSSSS 형식으로 저장됨)
        member.regTime = LocalDateTime.now(); // 회원 등록 시각(reg_time)
        return member;
    }

}
