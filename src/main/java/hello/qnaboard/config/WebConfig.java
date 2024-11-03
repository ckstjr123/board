package hello.qnaboard.config;

import hello.qnaboard.argumentresolver.LoginMemberArgumentResolver;
import hello.qnaboard.formatter.BoardTypeFormatter;
import hello.qnaboard.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableAsync
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인 체크 인터셉터
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/{boardType}/write", "/{boardType}/{boardId}/edit");
    }

    @Override
    public void addFormatters(FormatterRegistry registry ) {
        registry.addFormatter(new BoardTypeFormatter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
