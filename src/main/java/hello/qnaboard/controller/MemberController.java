package hello.qnaboard.controller;

import hello.qnaboard.controller.dto.MemberJoinForm;
import hello.qnaboard.controller.dto.ToEmail;
import hello.qnaboard.domain.Member;
import hello.qnaboard.exception.DuplicateMemberException;
import hello.qnaboard.exception.EmailAuthException;
import hello.qnaboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    /**
     * 회원가입 폼
     */
    @GetMapping("/new")
    public String joinForm(Model model) {
        model.addAttribute("memberJoinForm", new MemberJoinForm());
        return "members/memberJoinForm";
    }

    /**
     * ajax 이메일 인증번호 요청(회원가입 절차)
     * @param toEmail
     */
    @PutMapping(value = "/authno-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendAuthCode(@RequestBody @Valid ToEmail toEmail, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError("email").getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            this.memberService.sendAuthCodeToEmail(toEmail.toString()); // 인증을 요청한 이메일로 인증번호 전송
        }
         catch (DuplicateMemberException ex) {
            log.error("기존 회원에 대한 이메일 인증 요청 거부", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(toEmail + "으로 인증번호가 전송되었습니다.", HttpStatus.OK);
    }

    /**
     * 회원가입 요청 처리
     */
    @PostMapping("/new")
    public String join(@ModelAttribute @Valid MemberJoinForm memberJoinForm, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "members/memberJoinForm";
        }

        String name = memberJoinForm.getName();
        String email = memberJoinForm.getEmail(); // 회원가입 이메일
        String password = memberJoinForm.getPassword();

        /* 이메일 인증 체크 및 회원 저장 로직 호출 */
        try {
            boolean isAuthCodeMatch = this.memberService.verifyEmailAuthCode(email, memberJoinForm.getEmailAuthCode());
            Member member = isAuthCodeMatch ? Member.createMember(name, email, this.passwordEncoder.encode(password)) : null;
            if (member == null) {
                bindingResult.rejectValue("emailAuthCode", null, "인증 번호를 다시 확인해 주세요.");
                return "members/memberJoinForm";
            }

            this.memberService.saveMember(member);
            log.info("신규 회원가입: {}", member);
        }
         catch (EmailAuthException ex) {
            log.error("이메일 미인증 또는 인증번호 만료", ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            bindingResult.rejectValue("email", null, ex.getMessage());
            return "members/memberJoinForm";
        }
         catch (DuplicateMemberException ex) {
            log.error("중복 회원가입 요청", ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            bindingResult.reject(null, ex.getMessage());
            return "members/memberJoinForm";
        }

        return "redirect:/"; // 회원가입 성공하면 홈으로 리다이렉트
    }

}
