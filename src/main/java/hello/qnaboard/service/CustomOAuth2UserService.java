package hello.qnaboard.service;

import hello.qnaboard.dto.SessionMember;
import hello.qnaboard.domain.Member;
import hello.qnaboard.dto.oauth2userinfo.GoogleResponse;
import hello.qnaboard.dto.oauth2userinfo.NaverResponse;
import hello.qnaboard.dto.oauth2userinfo.OAuth2Response;
import hello.qnaboard.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

import static hello.qnaboard.dto.oauth2userinfo.provider.Provider.GOOGLE;
import static hello.qnaboard.dto.oauth2userinfo.provider.Provider.NAVER;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        log.info("oAuth2User.attributes: {}", userAttributes);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = this.getOAuth2Response(registrationId, oAuth2User);

        String email = oAuth2Response.getEmail();
        String name = oAuth2Response.getName();

        /* 닉네임도 통일해서 관리하고 회원 정보 수정을 통해 닉변 가능하도록 구현(닉네임 중복 허용) */
        Member member = this.findOrSaveUser(email, name);

        // oauth2 로그인 시 스프링 시큐리티가 세션에 저장하는 인증 객체: 'OAuth2AuthenticationToken'
        return new SessionMember(member.getId(), member.getName()); //principal
    }

    private OAuth2Response getOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        switch (registrationId) {
            case GOOGLE:
                return new GoogleResponse(oAuth2User);
            case NAVER:
                return new NaverResponse(oAuth2User);
//          case KAKAO:
//              return new KakaoResponse(oAuth2User);
            default:
                // OAuth2AuthorizationRequestRedirectFilter에서 처리할 수 없는 Registration Id에 대해 동일한 예외가 이미 발생하지만 명시해 둠
                throw new IllegalArgumentException("Invalid Client Registration with Id: " + registrationId);
        }
    }

    /**
     * 해당 유저가 있으면 반환, 없으면 유저를 저장하기 위해 생성
     * @param email
     * @param name
     * @return member
     */
    private Member findOrSaveUser(String email, String name) {
        Member member = this.memberMapper.findByEmail(email)
                .orElseGet(() -> Member.createMember(name, email, null));

        if (member.getId() == null) {
            // 소셜 계정을 통해 처음 회원가입하는 경우
            this.memberMapper.save(member);
        }
        return member;
    }

}
