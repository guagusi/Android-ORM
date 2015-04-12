package com.codeclub.lib.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Description:  �ֶ�
 * @author guagusi123@gmail.com
 * @date 2015��4��11�� ����12:36:59
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	
	boolean primaryKey() default false;
	
	// ������,���� 1
	boolean autoIncrease() default false;

	// �ֶ���
	String name() default "";
	
	// ��������
	ColumnType type();
	
	public enum ColumnType {
		// �з�������������ֵ�Ĵ�С��1��2��3��4��6 ��8�ֽڴ洢
		INTEGER,
		
		// ֵ���ı��ַ�����ʹ�����ݿ���루UTF-8, UTF-16BE �� UTF-16LE�����д洢
		TEXT,
		
		// ֵ��һ�����ݿ飬����������ԭ���洢
		BLOB, 
		
		// ����������8�ֽ� IEEE �������洢
		REAL,
		
		// ��ֵ
		NULL;
	}
}
