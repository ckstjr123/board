package hello.qnaboard.modelmapper;

import hello.qnaboard.domain.Member;
import hello.qnaboard.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.*;

/** 주의: ModelMapper는 변환할 클래스(destinationType)의 setter(세터)가 없는 필드에 대해서는 매핑이 안됨 */
public class ModelMapperTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void domainToDto() {
        //given
        MemberDto memberForm = new MemberDto();
        memberForm.setName("닉네임");
        memberForm.setEmail("email@test.com");
        memberForm.setPassword("password123");
        Member member = Member.createMember(memberForm); // 회원

        //when
        MemberDto memberDto = MemberDto.of(member); // Member → MemberDto

        //then
        assertThat(memberDto.getName()).isEqualTo(member.getName());
        assertThat(memberDto.getEmail()).isEqualTo(member.getEmail());
        assertThat(memberDto.getPassword()).isEqualTo(member.getPassword());
    }

}
