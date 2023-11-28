package hello.qnaboard.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 이메일 인증 번호는 숫자 6자리여야 함
 * @see EmailAuthCode
 */
public class EmailAuthCodeValidator implements ConstraintValidator<EmailAuthCode, String> {

    @Override
    public boolean isValid(String authCode, ConstraintValidatorContext context) {

        if ((authCode != null) && !authCode.isEmpty()) {
            try {
                Integer.parseInt(authCode);
            } catch (NumberFormatException ex) {
                return false;
            }
        } else {
            return false;
        }

        return (authCode.length() == 6);
    }
}
