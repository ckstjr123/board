package hello.qnaboard.domain;

import hello.qnaboard.constant.Role;
import hello.qnaboard.dto.MemberDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 사용자 / 관리자
 */
@Getter
public class Member {

    private Long id; // member_id

    private String name; // 닉네임

    private String email; // 이메일(고유)

    private String password;

    private Role role;

    private LocalDateTime regTime;

    private String modifiedBy;

    private LocalDateTime updateTime;

    public Member() {}

    /**
     * 회원 생성 메서드
     * @param memberForm
     * @return Member
     */
    public static Member createMember(MemberDto memberForm) {
        Member member = new Member();
        member.name = memberForm.getName();
        member.email = memberForm.getEmail();
        member.password = memberForm.getPassword(); // 패스워드 암호화 생략
        member.role = Role.USER;

        // 년-월-일 시:분:초:밀리초(6자리, 데이터베이스 timestamp의 ms 기본 자릿수는 6자리)
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSSSSS");
        String nowTime = LocalDateTime.now().format(format);
        member.regTime = LocalDateTime.parse(nowTime, format); // 회원 등록일(reg_time)이 됨
        return member;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

/*
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
*/

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Member member = (Member) object;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getName(), member.getName()) && Objects.equals(getEmail(), member.getEmail()) && Objects.equals(getPassword(), member.getPassword()) && getRole() == member.getRole() && Objects.equals(getRegTime(), member.getRegTime()) && Objects.equals(getModifiedBy(), member.getModifiedBy()) && Objects.equals(getUpdateTime(), member.getUpdateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPassword(), getRole(), getRegTime(), getModifiedBy(), getUpdateTime());
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", regTime=" + regTime +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

}
