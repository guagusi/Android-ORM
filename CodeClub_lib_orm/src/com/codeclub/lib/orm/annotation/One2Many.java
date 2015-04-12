package com.codeclub.lib.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Description: 一对多映射 
 * @author guagusi123@gmail.com
 * @date 2015年4月11日 上午12:36:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface One2Many {

	String foreignKey();
	
	String tableName();
}
