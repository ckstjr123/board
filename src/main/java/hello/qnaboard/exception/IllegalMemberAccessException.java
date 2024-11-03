package hello.qnaboard.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 동시 로그인 상태에서 회원 탈퇴 같은 처리가 이뤄진 뒤,
 * 세션을 유지 중인 클라이언트가 해당 회원과 연관된 리소스에 접근하면 발생시킬 수 있는 예외
 * (세션 파기 및 에러 응답)
 * @see ExceptionHandler
 */
public class IllegalMemberAccessException extends IllegalStateException {

    public IllegalMemberAccessException() {
        super("탈퇴된 계정입니다.");
    }

    public IllegalMemberAccessException(String message) {
        super(message);
    }

    public IllegalMemberAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
