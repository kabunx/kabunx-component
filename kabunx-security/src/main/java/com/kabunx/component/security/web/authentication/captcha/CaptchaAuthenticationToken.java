package com.kabunx.component.security.web.authentication.captcha;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private final Object principal;
    private String captcha;

    private String type;

    /**
     * 此构造函数用来初始化未授信凭据.
     *
     * @param principal the principal
     * @param captcha   the captcha
     * @param type      the type
     */
    public CaptchaAuthenticationToken(Object principal, String captcha, String type) {
        super(null);
        this.principal = principal;
        this.captcha = captcha;
        this.type = type;
        setAuthenticated(false);
    }

    /**
     * 此构造函数用来初始化授信凭据.
     *
     * @param principal   the principal
     * @param captcha     the captcha
     * @param authorities the authorities
     */
    public CaptchaAuthenticationToken(Object principal,
                                      String captcha,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.captcha = captcha;
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.captcha;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getType() {
        return type;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"
            );
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.captcha = null;
    }

}
