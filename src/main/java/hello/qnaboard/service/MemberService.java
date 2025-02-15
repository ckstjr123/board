package hello.qnaboard.service;

import hello.qnaboard.domain.Member;
import hello.qnaboard.dto.SessionMember;
import hello.qnaboard.exception.DuplicateMemberException;
import hello.qnaboard.exception.EmailAuthException;
import hello.qnaboard.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    private static final String EMAIL_AUTH_KEY_PREFIX = "auth_code#";
    private static final int EMAIL_AUTH_CODE_LEN = 6;

    /**
     * 회원가입 시 이메일 인증 요청
     * @param toEmail
     * @throws DuplicateMemberException
     */
    public void sendAuthCodeToEmail(String toEmail) {
        // 이미 가입된 회원이면 인증번호 발급 X
        this.validateDuplicateMember(toEmail);

        String title = "회원가입 인증 번호";
        String authCode = this.generateEmailAuthCode(); // 인증번호 생성
        this.emailService.sendEmail(toEmail, title, authCode); // 사용자 이메일로 인증번호 전송(비동기)
        // 인증 요청 이메일에 대한 인증 번호 Redis에 저장(3분 뒤 만료), 회원가입 요청 시 인증번호 검증
        this.redisUtil.setDataExpire(EMAIL_AUTH_KEY_PREFIX + toEmail, authCode, 120);
    }

    /**
     * 이메일 인증 번호(난수 6자리) 생성
     * @return {@code String} newEmailAuthcode
     */
    private String generateEmailAuthCode() {
        StringBuilder sb = new StringBuilder();
        try {
            Random numberGenerator = SecureRandom.getInstance("SHA1PRNG");

            for (int i = 0; i < EMAIL_AUTH_CODE_LEN; i++) {
                sb.append(numberGenerator.nextInt(10)); // 0부터 10 미만 중
            }
        } catch (NoSuchAlgorithmException ex) {
            // should not happen
        }

        return sb.toString();
    }

    /**
     * 이메일을 통해 이미 가입된 회원인지 확인
     * @param email
     * @throws DuplicateMemberException
     */
    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = this.memberMapper.findByEmail(email);

        if (findMember.isPresent()) {
            throw new DuplicateMemberException("이미 가입된 이메일입니다.");
        }
    }

    /**
     * 신규 회원가입 이메일에 대해 발급된 인증번호가 있으면 반환
     * @param email
     * @return {@code String} emailAuthCode
     * @throws DuplicateMemberException
     * @throws EmailAuthException
     */
    private String findAuthCodeAbout(String email) {
        this.validateDuplicateMember(email); //기존 회원에겐 인증번호가 발급되지 않으므로 예외

        String authCodeKey = EMAIL_AUTH_KEY_PREFIX + email;

        String emailAuthCode = this.redisUtil.getValue(authCodeKey); // 인증 요청 이메일에 대한 인증번호 조회
        if (emailAuthCode == null) {
            // 요청 이메일에 대한 인증번호가 유효하지 않음
            throw new EmailAuthException("미인증 또는 인증번호 만료, 이메일 인증을 요청해주세요.");
        }

        return emailAuthCode;
    }

    /**
     * 이메일 인증번호 일치 여부 검증
     *
     * @param email
     * @param authCode
     * @return {@code boolean} isAuthCodeMatch
     * @throws DuplicateMemberException
     * @throws EmailAuthException
     */
    public boolean verifyEmailAuthCode(String email, String authCode) {
        String emailAuthCode = this.findAuthCodeAbout(email); // 이메일 인증번호

        boolean isAuthCodeMatch = authCode.equals(emailAuthCode); // 인증번호가 일치하는지 확인
        if (isAuthCodeMatch) {
            // 인증번호 일치, 레디스에 저장된 해당 이메일 인증번호 데이터 삭제
            this.redisUtil.deleteData(EMAIL_AUTH_KEY_PREFIX + email);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Optional<Member> findById(Long id) {
        return this.memberMapper.findById(id);
    }

    /**
     * 회원가입 처리
     * @param member
     * @return {@code Long} savedMemberId
     */
    @Transactional(readOnly = false)
    public Long saveMember(Member member) {
        this.memberMapper.save(member);
        return member.getId();
    }

    /**
     * 이메일로 조회해서 반환한 유저 정보를 토대로 DaoAuthenticationProvider에서 로그인 검증이 수행됨
     * @param email
     * @return userDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 해당 이메일로 가입된 유저 조회, 존재하지 않으면 예외
        Member member = this.memberMapper.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        SessionMember userDetails = new SessionMember(member.getId(), member.getName());
        userDetails.setPassword(member.getPassword()); // 로그인 검증에 사용될 비밀번호 세팅. 인증을 완료하면 null로 초기화 됨
        return userDetails;
    }

/*    *//**
     * 닉네임 중복 체크
     * @param nickname
     * @return 중복 닉네임이면 true
     *//*
    @Transactional
    public boolean checkDuplicateName(String nickname) {
        return this.memberMapper.existsByName(nickname);
    }
*/
}
