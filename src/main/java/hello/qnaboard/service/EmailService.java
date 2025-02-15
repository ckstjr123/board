package hello.qnaboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender; // 스프링 부트 자동 빈 등록


    /**
     * @param toEmail
     * @param title
     * @param text
     * @throws org.springframework.mail.MailException
     */
    @Async
    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = this.generateEmailForm(toEmail, title, text);
        this.mailSender.send(emailForm);
    }

    /**
     * 전송할 이메일 메시지(수신자 이메일, 이메일 제목, 이메일 내용) 세팅
     * @param toEmail
     * @param title
     * @param text
     * @return SimpleMailMessage
     */
    private SimpleMailMessage generateEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(toEmail);
        emailMessage.setSubject(title);
        emailMessage.setText(text);

        return emailMessage;
    }

}
