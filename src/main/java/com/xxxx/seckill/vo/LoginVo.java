package com.xxxx.seckill.vo;

import com.sun.istack.internal.NotNull;
import com.xxxx.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginVo {
    @NotNull
    @IsMobile()   //默认required是true,如果参数可为空时，@IsMobile(required=false)
    private String mobile;

    @NotNull //(参数验证不为空)
    @Length(min=32)   //最小32位
    private String password;
}
