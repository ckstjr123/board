package hello.qnaboard.service;

import hello.qnaboard.domain.Member;
import hello.qnaboard.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberMapper memberMapper;

    /**
     * 해당 이메일 및 패스워드와 일치하는 회원
     * @param email
     * @param password
     * @return Member
     */
    public Member login(String email, String password) {
        return this.memberMapper.findByEmail(email)
                .filter(member -> member.getPassword().equals(password)) // 해당 이메일을 가진 회원이 있으면 패스워드 일치 여부 확인
                .orElse(null);
    }
}
