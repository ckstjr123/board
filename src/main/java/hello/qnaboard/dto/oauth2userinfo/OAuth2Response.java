package hello.qnaboard.dto.oauth2userinfo;

public interface OAuth2Response {

    String getProvider(); // 제공자 (ex. naver, google, ...)

    String getProviderId(); // 제공자가 발급해주는 id

    String getEmail();

    String getName();
}
