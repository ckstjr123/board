package hello.qnaboard.controller;

import hello.qnaboard.constant.SessionConst;
import hello.qnaboard.domain.Member;
import hello.qnaboard.dto.LoginForm;
import hello.qnaboard.dto.MemberDto;
import hello.qnaboard.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 로그인 및 로그아웃 요청 처리
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    /**
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm(LoginForm form, Model model) {
        model.addAttribute("loginForm", form);
        return "login/loginForm";
    }

    /**
     * 로그인
     * - 로그인 직전에 요청했던 페이지 있으면 해당 페이지로 리다이렉트, 그외 홈으로 리다이렉트
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            bindingResult.reject("loginFail", "이메일 또는 비밀번호를 잘못 입력했습니다.");
            return "login/loginForm";
        }

        Member loginMember = this.loginService.login(loginForm.getEmail(), loginForm.getPassword());

        // 해당 이메일로 가입된 회원이 없거나 패스워드가 틀림
        if (loginMember == null) {
            bindingResult.reject("loginFail", "이메일 또는 비밀번호를 잘못 입력했습니다.");
            return "login/loginForm";
        }

        /* 로그인 성공 */
        HttpSession session = request.getSession(); // 기존 세션이 존재하면 반환, 없으면 신규 세션 생성
        MemberDto loginMemberDto = MemberDto.of(loginMember);
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMemberDto); // 세션에 로그인 회원 정보 저장(replace or add this attribute)

        log.info("로그인: {}, 세션 ID: {}", loginMember.getEmail(), session.getId());
        return "redirect:" + redirectURL;
    }


    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 해당 세션 만료시킴
        }

        return "redirect:/";
    }

}
