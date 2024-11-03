package hello.qnaboard.repository;

import hello.qnaboard.domain.Member;
import hello.qnaboard.repository.dto.MemberUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/** MemberDAO */
@Mapper
public interface MemberMapper {
    boolean existsByName(String name);

    void save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email); // 이메일은 고유함

    void update(@Param("id") Long id, @Param("updateParam") MemberUpdateDto updateParam);
}
