package hello.qnaboard.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 로그인 회원 정보를 담은 세션 보관용 객체
 * (implements Serializable: 레디스에 세션 데이터로 저장하기 위함)
 */
@Getter
@EqualsAndHashCode
public final class SessionMember implements UserDetails, CredentialsContainer, OAuth2User, Serializable {

    private final Long memberId; // 로그인 사용자 id(pk)
    private final String name; // 닉네임
//    private final List<GrantedAuthority> authorities;

    //password: 폼 로그인 시 검증에 사용됨
    //인증을 마치면 eraseCredentials()에 의해 null로 초기화되어 세션에 저장됨.
    private String password;

    public SessionMember(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }


    @Override
    public String getUsername() {
        // oauth2 로그인 시 사용됨
        return this.name;
    }

    @Override
    public void eraseCredentials() {
        this.password = null; //
    }

    public void setPassword(String password) {
        Assert.notNull(password, "FormLogin password cannot be null"); //
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }


    //--------------------------------oauth2Login--------------------------------//

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(this.role.toString()));
        return List.of();
    }
}