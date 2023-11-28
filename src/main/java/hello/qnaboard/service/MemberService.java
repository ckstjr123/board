package hello.qnaboard.service;

import hello.qnaboard.domain.Member;
import hello.qnaboard.exception.DuplicateMemberException;
import hello.qnaboard.exception.EmailAuthException;
import hello.qnaboard.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberMapper memberMapper;
    private final EmailService emailService;
    private final RedisUtil redisUtil;
    public static final String EMAIL_AUTH_KEY_PREFIX = "auth_code#";

    /**
     * 회원가입 시 이메일 인증 요청
     * @param toEmail
     * @throws DuplicateMemberException
     * @throws org.springframework.mail.MailException 인증번호 전송 실패
     */
    public void sendAuthCodeToEmail(String toEmail) {
        // 이미 가입된 회원이면 인증번호 발급 X
        this.validateDuplicateMember(toEmail); 

        String title = "회원가입 인증 번호";
        String authCode = this.generateEmailAuthCode(); // 인증번호 생성
        this.emailService.sendEmail(toEmail, title, authCode); // 사용자 이메일로 인증번호 전송
        // 인증 요청 이메일에 대한 인증 번호 Redis에 저장(3분 뒤 만료), 회원가입 요청 시 인증번호 검증
        this.redisUtil.setDataExpire(EMAIL_AUTH_KEY_PREFIX + toEmail, authCode, 120);
    }

    /**
     * 이메일 인증 번호(랜덤 6자리 난수) 생성
     * @return {@code String} newEmailAuthcode
     */
    private String generateEmailAuthCode() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        int length = 6; // 인증번호는 6자리
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0부터 10 미만
        }
        return sb.toString();
    }

    /**
     * 이메일을 통해 이미 가입된 회원인지 확인
     * @param email
     * @throws DuplicateMemberException
     */
    @Transactional(readOnly = true)
    private void validateDuplicateMember(String email) {
        Optional<Member> findMember = this.memberMapper.findByEmail(email);

        if (findMember.isPresent()) {
            throw new DuplicateMemberException("이미 가입된 회원입니다.");
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
        // 기존 회원에게는 인증번호가 발급되지 않기 때문에 예외
        this.validateDuplicateMember(email);

        String authCode_key = EMAIL_AUTH_KEY_PREFIX + email;

        String emailAuthCode = this.redisUtil.getValue(authCode_key); // 인증 요청 이메일에 대한 인증번호 조회
        if (emailAuthCode == null) {
            // 요청 이메일에 대한 인증번호가 유효하지 않음
            throw new EmailAuthException("미인증 또는 인증 번호가 만료되었습니다. 이메일 인증을 요청해주세요.");
        }

        return emailAuthCode;
    }

    /**
     * 이메일 인증번호 일치 여부 검증
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
        }
        else {
            return false; // 인증번호 불일치
        }
    }

    /**
     * 회원가입 처리
     * @param member
     * @return {@code Long} savedMemberId
     */
    public Long saveMember(Member member) {
//        this.validateDuplicateMember(member.getEmail());
        this.memberMapper.save(member);
        return member.getId();
    }

}
