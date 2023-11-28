package hello.qnaboard.exception;

/**
 * 이미 가입한 회원에 대한 이메일 인증 / 회원가입 요청일 때
 */
public class DuplicateMemberException extends IllegalStateException {

    public DuplicateMemberException(String message) {
        super(message);
    }
    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
