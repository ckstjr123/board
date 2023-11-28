package hello.qnaboard.exception;

/**
 * 이메일 인증 미요청 또는 인증번호 만료로 인한 이메일 인증 실패 예외
 */
public class EmailAuthException extends IllegalStateException {

    public EmailAuthException(String message) {
        super(message);
    }
    public EmailAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
