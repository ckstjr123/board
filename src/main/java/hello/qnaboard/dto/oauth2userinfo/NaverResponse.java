package hello.qnaboard.dto.oauth2userinfo;

import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Map;
import static hello.qnaboard.dto.oauth2userinfo.provider.Provider.NAVER;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverResponse(OAuth2User oAuth2User) {
        this.attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
    }

    @Override
    public String getProvider() {
        return NAVER;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("nickname").toString();
    }
}
