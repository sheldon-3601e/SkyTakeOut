package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName AutoFill
 * @Author 26483
 * @Date 2023/11/10 16:23
 * @Version 1.0
 * @Description 自定义注解，表示公共字段填充
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //指定数据库操作类型 INSERT UPDATE
    OperationType value();
}
