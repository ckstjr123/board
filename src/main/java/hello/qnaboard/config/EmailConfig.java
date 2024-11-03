package hello.qnaboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * application.yml에 설정한 환경 변수들을 활용해서 JavaMailSenderImpl 객체 생성
 * 스프링 부트가 설정 정보를 읽어서 JavaMailSenderImpl 객체 자동 생성 및 스프링 빈 등록
 */
//@Configuration 
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    //
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;


    /**
     * JavaMailSender 인터페이스는 JavaMail API를 사용하여 이메일을 전송하는 데 사용됨.
     * JavaMailSenderImpl 객체를 통해 이메일을 전송할 수 있음.
     * @return JavaMailSender
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(this.host);
        emailSender.setPort(this.port);
        emailSender.setUsername(this.username);
        emailSender.setPassword(this.password);
        emailSender.setDefaultEncoding("UTF-8");
        emailSender.setJavaMailProperties(this.getMailProperties());

        return emailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", this.auth);
        properties.put("mail.smtp.starttls.enable", this.starttlsEnable);
        properties.put("mail.smtp.starttls.required", this.starttlsRequired);
        properties.put("mail.smtp.connectiontimeout", this.connectionTimeout);
        properties.put("mail.smtp.timeout", this.timeout);
        properties.put("mail.smtp.writetimeout", this.writeTimeout);

        return properties;
    }

}