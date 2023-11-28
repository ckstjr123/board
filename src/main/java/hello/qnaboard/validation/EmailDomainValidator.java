package hello.qnaboard.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 허용 가능한 이메일 도메인: naver.com, gmail.com
 * @see EmailDomain
 */
public class EmailDomainValidator implements ConstraintValidator<EmailDomain, String> {
    @Override
    public boolean isValid(String toEmail, ConstraintValidatorContext context) {
        if ((toEmail == null) || (toEmail.isEmpty())) {
            context.disableDefaultConstraintViolation(); // 디폴트 메시지 비활성화
            context.buildConstraintViolationWithTemplate("이메일을 입력해주세요.").addConstraintViolation();
            return false;
        }

        int at = toEmail.indexOf("@");
        if (at == -1) {
            return false;
        }
        /* 위 로직들을 생략하려면 @GroupSequence를 통해 @NotEmpty, @Email과 함께 검증 순서를 지정해야 함 */

        String emailDomain = toEmail.substring(at + 1);
        return emailDomain.equals("naver.com") || emailDomain.equals("gmail.com");
    }
}
