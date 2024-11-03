package hello.qnaboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Not found the enum constant of hello.qnaboard.constant.BoardType with the specified name
 * @see ResponseStatus
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundBoardTypeException extends IllegalArgumentException {

    public NotFoundBoardTypeException(String message) {
        super(message);
    }

    public NotFoundBoardTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
