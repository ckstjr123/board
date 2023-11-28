package hello.qnaboard.repository;

import hello.qnaboard.domain.Member;
import hello.qnaboard.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/** MemberDAO */
@Mapper
public interface MemberMapper {

    void save(Member member);

    Optional<Member> findByEmail(String email); // 이메일은 고유함

    void update(@Param("id") Long id, @Param("updateParam") MemberDto updateParam);
}
