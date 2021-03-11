package com.xxxx.seckill.vo;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.validator.IsMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            //判断是否必填，必填时,直接检查验证规则
            return ValidatorUtil.isMobile(value);
        }else{
            //参数非必填时，未传则返回true
            return StringUtils.isBlank(value) ? true:ValidatorUtil.isMobile(value);
        }
    }
}
