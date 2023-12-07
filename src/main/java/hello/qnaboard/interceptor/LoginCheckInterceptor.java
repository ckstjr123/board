package hello.qnaboard.interceptor;

import hello.qnaboard.constant.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 인증 체크 인터셉터
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("로그인 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false); // 세션이 존재하는지 확인

        // 비로그인 사용자
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            // 요청한 url을 쿼리 파라미터로 담아 로그인 페이지로 리다이렉트
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }

}
