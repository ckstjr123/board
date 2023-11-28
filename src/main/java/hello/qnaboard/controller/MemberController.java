package hello.qnaboard.controller;

import hello.qnaboard.domain.Member;
import hello.qnaboard.dto.MemberDto;
import hello.qnaboard.exception.DuplicateMemberException;
import hello.qnaboard.exception.EmailAuthException;
import hello.qnaboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 폼
     */
    @GetMapping("/new")
    public String joinForm(Model model) {
        model.addAttribute("memberForm", new MemberDto());
        return "members/memberJoinForm";
    }

    /**
     * ajax 이메일 인증 요청(회원가입 절차)
     * @param toEmail
     */
    @PostMapping("/auth-email")
    public ResponseEntity<String> sendAuthCode(@Valid @RequestBody ToEmail toEmail, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError("email").getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            this.memberService.sendAuthCodeToEmail(toEmail.toString()); // 인증을 요청한 이메일로 인증번호 전송
        }
         catch (DuplicateMemberException ex) {
            log.info("기존 회원에 대한 이메일 인증 요청 거부", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
         catch (MailException ex) {
            log.info("이메일 인증번호 전송 실패", ex);
            return new ResponseEntity<>("인증번호 전송에 실패했습니다. " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(toEmail + " 으로 인증번호가 전송되었습니다.", HttpStatus.OK);
    }

    /**
     * 회원가입 요청 처리
     */
    @PostMapping("/new")
    public String join(@Valid @ModelAttribute("memberForm") MemberDto memberForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "members/memberJoinForm";
        }

        String email = memberForm.getEmail(); // 회원가입 이메일
        String authCode = memberForm.getEmailAuthCode(); // 검증할 인증번호

        /* 이메일 인증번호 검증 및 회원가입 로직 호출 */
        try {
            boolean isAuthCodeMatch = this.memberService.verifyEmailAuthCode(email, authCode);
            Member member = isAuthCodeMatch ? Member.createMember(memberForm) : null;
            if (member == null) {
                // 이메일 인증번호 불일치
                bindingResult.rejectValue("emailAuthCode", null, "인증 번호가 틀립니다.");
                return "members/memberJoinForm";
            }

            this.memberService.saveMember(member);
            log.info("신규 회원가입: {}", member);
        }
         catch (EmailAuthException ex) {
            log.info("이메일 미인증 또는 인증번호 만료", ex);
            bindingResult.rejectValue("email", null, ex.getMessage());
            return "members/memberJoinForm";
        }
         catch (DuplicateMemberException ex) {
            log.info("중복 회원가입 요청", ex);
            bindingResult.reject(null, ex.getMessage());
            return "members/memberJoinForm";
        }

        return "redirect:/"; // 회원가입 성공 후 홈으로 리다이렉트
    }

}
