package hello.qnaboard.dto.oauth2userinfo;

import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Map;
import static hello.qnaboard.dto.oauth2userinfo.provider.Provider.GOOGLE;

public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public GoogleResponse(OAuth2User oAuth2User) {
        this.attributes = oAuth2User.getAttributes();
    }

    @Override
    public String getProvider() {
        return GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
