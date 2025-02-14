package hello.qnaboard.controller;

import hello.qnaboard.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal SessionMember loginMember, Model model) {
        // 비로그인 사용자 및 세션에 회원 데이터가 없는 경우 기본 홈,
        // 로그인 사용자면 로그인 회원 전용 홈 화면으로 렌더링됨
        model.addAttribute("loginMember", loginMember);
        return "home";
    }

}
