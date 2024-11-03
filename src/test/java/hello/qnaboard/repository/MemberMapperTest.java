package hello.qnaboard.repository;

import hello.qnaboard.domain.Member;
import hello.qnaboard.controller.dto.MemberJoinForm;
import hello.qnaboard.repository.dto.MemberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("회원 등록")
    void save() {
        //given
        Member member = this.createMember();

        //when
        this.memberMapper.save(member);

        //then
        Member findMember = this.memberMapper.findById(member.getId()).orElseThrow();
        log.info("savedMember: {}", member);
        log.info("findMember: {}", findMember);

        // usingRecursiveComparison(): 동등성 비교,
        // withStrictTypeChecking(): actual과 expected 타입이 동일할 때만 비교(기본 설정은 필드만 같다면 DTO와도 비교 가능)
        assertThat(findMember).usingRecursiveComparison()
                .withStrictTypeChecking()
                .isEqualTo(member);
    }
    
    @Test
    @DisplayName("회원 수정")
    void update() {
        //given
        Member member = this.createMember();
        this.memberMapper.save(member);
        Long memberId = member.getId();

        //when: 회원정보 수정
        MemberUpdateDto updateParam = new MemberUpdateDto();
        updateParam.setName("변경한 닉네임");
        updateParam.setPassword("비밀번호변경123!");
        updateParam.setUpdateTime(LocalDateTime.now());

        this.memberMapper.update(memberId, updateParam);

        //then
        Member findMember = this.memberMapper.findByEmail(member.getEmail()).get();
        log.info("updatedMember: {}", findMember);

        assertThat(findMember.getName()).isEqualTo(updateParam.getName());
        assertThat(findMember.getPassword()).isEqualTo(updateParam.getPassword());
        assertThat(findMember.getUpdateTime()).isEqualTo(updateParam.getUpdateTime());
    }

    @Test
    @DisplayName("닉네임 중복 확인")
    void existsByName() {
        Member member = this.createMember();
        this.memberMapper.save(member);

        assertThat(this.memberMapper.existsByName("존재하지 않는 닉네임")).isFalse();
        assertThat(this.memberMapper.existsByName(member.getName())).isTrue();
    }

    private Member createMember() {
        MemberJoinForm memberJoinForm = new MemberJoinForm();
        memberJoinForm.setName("닉네임");
        memberJoinForm.setEmail("email@test.com");
        memberJoinForm.setPassword("password123");
        return Member.createMember(memberJoinForm.getName(), memberJoinForm.getEmail(), memberJoinForm.getPassword());
    }

}
