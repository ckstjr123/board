package hello.qnaboard.exception;

/**
 * 인증된 사용자가 대상 리소스에 대한 권한이 없을 때 발생하는 예외
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
