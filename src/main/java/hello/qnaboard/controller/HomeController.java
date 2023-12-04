package hello.qnaboard.controller;

import hello.qnaboard.constant.SessionConst;
import hello.qnaboard.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberDto loginMemberDto,
                       Model model) {
        // 비로그인 사용자 및 세션에 회원 데이터가 없는 경우 일반 홈으로
        if (loginMemberDto == null) {
            return "home";
        }

        // 세션 유지 중이면 로그인 회원 전용 홈 화면
        model.addAttribute("loginMember", loginMemberDto);
        return "home";
    }

}
