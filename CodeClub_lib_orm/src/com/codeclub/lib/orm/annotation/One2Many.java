package com.codeclub.lib.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Description: һ�Զ�ӳ�� 
 * @author guagusi123@gmail.com
 * @date 2015��4��11�� ����12:36:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface One2Many {

	String foreignKey();
	
	String tableName();
}
