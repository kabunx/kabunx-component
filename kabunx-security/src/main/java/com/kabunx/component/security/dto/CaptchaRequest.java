package com.kabunx.component.security.dto;

import com.kabunx.component.common.dto.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CaptchaRequest extends DTO {
    private String phone;
    private String captcha;
    private String type;
}
