package com.codeclub.lib.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Description:  字段
 * @author guagusi123@gmail.com
 * @date 2015年4月11日 上午12:36:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	
	boolean primaryKey() default false;
	
	// 自增长,步长 1
	boolean autoIncrease() default false;

	// 字段名
	String name() default "";
	
	// 数据类型
	ColumnType type();
	
	public enum ColumnType {
		// 有符号整数，根据值的大小以1，2，3，4，6 或8字节存储
		INTEGER,
		
		// 值是文本字符串，使用数据库编码（UTF-8, UTF-16BE 或 UTF-16LE）进行存储
		TEXT,
		
		// 值是一个数据块，按它的输入原样存储
		BLOB, 
		
		// 浮点数，以8字节 IEEE 浮点数存储
		REAL,
		
		// 空值
		NULL;
	}
}
