package com.codeclub.lib.orm;

import java.lang.reflect.Field;

import android.text.TextUtils;
import android.util.Log;

import com.codeclub.lib.orm.annotation.Column;
import com.codeclub.lib.orm.annotation.Table;
import com.codeclub.lib.orm.annotation.Column.ColumnType;

/**
 * 
 * @Description: 
 * @author guagusi123@gmail.com
 * @date 2015年4月12日 下午7:08:55
 */
public class CacheHelper {

	/**
	 * 字段名。注解优先
	 * @param field
	 * @return
	 */
	public static String getColumnName(Field field) {
		String columnName = field.getAnnotation(Column.class).name();
		if(TextUtils.isEmpty(columnName)) {
			columnName = field.getName();
		}
		return columnName;
	}
	
	/**
	 * 字段类型。注解优先
	 * @param field
	 * @return
	 */
	public static String getColumnType(Field field) {
		String type = null;
		ColumnType columnType = field.getAnnotation(Column.class).type();
		if(columnType == null) {
			Class<?> fieldType = field.getType();
			if(fieldType == String.class) {
				columnType = ColumnType.TEXT;
			} else if(fieldType == Integer.class || fieldType == int.class || 
					fieldType == Long.class || fieldType == long.class ||
					fieldType == short.class || fieldType == Short.class) {
				columnType = ColumnType.INTEGER;
			} else if(fieldType == double.class || fieldType == Double.class) {
				columnType = ColumnType.REAL;
			} else if(fieldType == float.class || fieldType == Float.class) {
				columnType = ColumnType.REAL;
			} else if(fieldType == byte[].class || fieldType == Byte[].class) {
				columnType = ColumnType.BLOB;
			}
		}
		
		switch(columnType) {
			case NULL:
				type = "NULL";
				break;
			case INTEGER:
				type = "INTEGER";
				break;
			case TEXT:
				type = "TEXT";
				break;
			case REAL:
				type = "REAL";
				break;
			case BLOB:
				type = "BLOB";
				break;
			default:
				break;
		}
		
		return type;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public static Class<?> getFieldType(Field field) {
		Class<?> fieldType = field.getType();
		return fieldType;
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		if(!clazz.isAnnotationPresent(Table.class)) {
			String errorMsg = "缺少 Table 注解";
			Log.e("ORM", errorMsg);
			throw new RuntimeException(errorMsg);
		}
		
		String tableName = clazz.getAnnotation(Table.class).name();
		if(TextUtils.isEmpty(tableName)) {
			tableName = clazz.getSimpleName();
		}
		
		return tableName;
	}
}
