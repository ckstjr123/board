package hello.qnaboard.domain;

import hello.qnaboard.dto.MemberDto;
import hello.qnaboard.repository.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    void save() {
        //given
        Member member = this.createMember();

        //when
        this.memberMapper.save(member);

        //then
        Member findMember = this.memberMapper.findByEmail(member.getEmail()).get();

        System.out.println("savedMember: " + member);
        System.out.println("findMember: " + findMember);

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void update() {
        //given
        Member member = this.createMember();
        this.memberMapper.save(member);
        Long memberId = member.getId();

        //when: 회원정보 수정
        MemberDto updateParam = new MemberDto();
        updateParam.setName("변경된 닉네임");
        updateParam.setPassword("비밀번호변경123!");
        updateParam.setModifiedBy("수정자");
        updateParam.setUpdateTime(this.getFormattedNowTime()); // 수정 시각 세팅
        this.memberMapper.update(memberId, updateParam);

        //then
        Member findMember = this.memberMapper.findByEmail(member.getEmail()).get();
        System.out.println("updatedMember: " + findMember);

        assertThat(findMember.getName()).isEqualTo(updateParam.getName());
        assertThat(findMember.getPassword()).isEqualTo(updateParam.getPassword());
        assertThat(findMember.getModifiedBy()).isEqualTo(updateParam.getModifiedBy());
        assertThat(findMember.getUpdateTime()).isEqualTo(updateParam.getUpdateTime());
    }


    private Member createMember() {
        MemberDto memberForm = new MemberDto();
        memberForm.setName("닉네임");
        memberForm.setEmail("email@test.com");
        memberForm.setPassword("password123");
        return Member.createMember(memberForm); // 회원
    }

    private LocalDateTime getFormattedNowTime() {
        // 년-월-일 시:분:초:밀리초(6자리, 데이터베이스 timestamp의 ms 기본 자릿수는 6자리)
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSSSSS");
        String nowTime = LocalDateTime.now().format(format);
        return LocalDateTime.parse(nowTime, format);
    }

}
