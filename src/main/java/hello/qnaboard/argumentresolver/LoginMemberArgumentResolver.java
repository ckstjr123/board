package hello.qnaboard.argumentresolver;

import hello.qnaboard.constant.SessionConst;
import hello.qnaboard.dto.SessionMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 세션이 없거나 세션에 회원 데이터(SessionMember)가 없으면 null이 리턴되며,
 * 유지 중인 세션에 회원 데이터가 있으면 로그인 사용자 정보가 담긴 SessionMember 객체가 반환됨
 * @see Login
 * @see HandlerMethodArgumentResolver
 */
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("LoginMemberArgumentResolver.supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean isSessionMemberType = parameter.getParameterType() == SessionMember.class;

        return hasLoginAnnotation && isSessionMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("LoginMemberArgumentResolver.resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER); //SessionMember
    }
}
