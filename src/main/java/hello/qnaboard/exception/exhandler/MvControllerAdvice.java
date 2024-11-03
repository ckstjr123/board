package hello.qnaboard.exception.exhandler;

import hello.qnaboard.exception.AccessDeniedException;
import hello.qnaboard.exception.IllegalMemberAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;

@Slf4j
@ControllerAdvice
public class MvControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String emptyResultDataAccessExHandler(EmptyResultDataAccessException ex) {
        log.error("[ExceptionHandler] emptyResultDataAccessExHandler", ex);
        return "error/404";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalMemberAccessException.class)
    public String illegalMemberAccessExHandler(IllegalMemberAccessException ex, HttpSession session, Model model) {
        log.error("[ExceptionHandler] illegalMemberAccessExHandler", ex);
        session.invalidate(); // 세션 파기
        model.addAttribute("exceptionMsg", ex.getMessage());
        return "error/4xx";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedExHandler(AccessDeniedException ex, Model model) {
        log.error("[ExceptionHandler] accessDeniedExHandler", ex);
        model.addAttribute("exceptionMsg", ex.getMessage());
        return "error/403";
    }

}
