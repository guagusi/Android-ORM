package com.codeclub.lib.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Description: ����id ��int
 * @author guagusi123@gmail.com
 * @date 2015��4��11�� ����1:20:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {

	// ������,���� 1
	boolean autoIncrease() default true;
}
