package com.codeclub.lib.orm;

import java.lang.reflect.Field;

import android.text.TextUtils;
import android.util.Log;

import com.codeclub.lib.orm.annotation.Column;
import com.codeclub.lib.orm.annotation.Column.ColumnType;
import com.codeclub.lib.orm.annotation.Table;

/**
 * 
 * @Description: sql ��乤����
 * @author guagusi123@gmail.com
 * @date 2015��4��11�� ����3:08:29
 */
public class SQLUtils {
	
	public static final String[] createTablesSQL(String[] tables) {
		if(tables == null || tables.length <= 0) {
			return null;
		}
		
		String[] tablesSQL = new String[tables.length];
		
		
		for(int i = 0; i < tables.length; i ++) {
			StringBuilder sqlSb = new StringBuilder();
			String table = tables[i];
			Class<?> clazz;
			try {
				clazz = Class.forName(table);
			} catch (ClassNotFoundException e) {
				Log.e("ORM", "û���ҵ���" + table);
				continue;
			}
			
			
			createTableSQL(clazz, sqlSb);
			tablesSQL[i] = sqlSb.toString();
		}
		
		return tablesSQL;
	}
	
	/**
	 * ���ɽ������
	 * @param clazz
	 * @return
	 */
	public static final String createTableSQL(Class<?> clazz, StringBuilder sqlSb) {
		if(clazz == null || !clazz.isAnnotationPresent(Table.class)) {
			return null;
		}
		
		if(sqlSb == null) {
			sqlSb = new StringBuilder();
		}
		String tableName = getTableName(clazz);
		sqlSb.append("create table if not exists " + tableName + " ( ");
		
		Field[] fields = clazz.getFields();
		for(int j = 0; j < fields.length; j ++) {
			if(j == fields.length - 1) {
				getColumnSQL(fields[j], true, sqlSb);
			} else {
				getColumnSQL(fields[j], false, sqlSb);
			}
		}
		
		sqlSb.append(" );");
		
		return sqlSb.toString();
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static final String clearTableSQL(Class<?> clazz) {
		String sql = " delete from table " + getTableName(clazz);
		return sql;
	}
	
	/**
	 * ɾ�����
	 * @param clazz
	 * @return
	 */
	public static final String dropTableSQL(Class<?> clazz) {
		String sql = " drop table if exists " + getTableName(clazz);
		return sql;
	}
	
	/**
	 * ��ȡ���ݱ�����ע�����ȣ�ע�ⲻָ����ȡ����
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		if(!clazz.isAnnotationPresent(Table.class)) {
			String errorMsg = "ȱ�� Table ע��";
			Log.e("ORM", errorMsg);
			throw new RuntimeException(errorMsg);
		}
		
		String tableName = clazz.getAnnotation(Table.class).name();
		if(TextUtils.isEmpty(tableName)) {
			tableName = clazz.getSimpleName();
		}
		return tableName;
	}
	
	/**
	 * �ֶε�sql ���
	 * @param field
	 * @param isLast
	 * @return
	 */
	private static void getColumnSQL(Field field, boolean isLast, StringBuilder sqlSb) {
		if(field == null || !field.isAnnotationPresent(Column.class)) {
			return;
		}
		sqlSb.append(" " + getColumnName(field) + " ");
		sqlSb.append(" " + getColumnType(field) + " ");
		// primary key
		if(field.getAnnotation(Column.class).primaryKey()) {
			sqlSb.append(" primary key ");
			if(field.getAnnotation(Column.class).autoIncrease()) {
				sqlSb.append(" autoincrement ");
			}
		}
		
		if(!isLast) {
			sqlSb.append(" , ");
		}
	}
	
	/**
	 * �ֶ�����ע������
	 * @param field
	 * @return
	 */
	public static String getColumnName(Field field) {
		return AnnoAndRefleCache.getAnnoAndRefleCache().getRAMCacheColumnName(field);
		
	}
	
	/**
	 * �ֶ����͡�ע������
	 * @param field
	 * @return
	 */
	private static String getColumnType(Field field) {
		return AnnoAndRefleCache.getAnnoAndRefleCache().getRAMCacheColumnType(field);
	}
	
	/**
	 * 
	 * @return
	 */
	public static final Class<?> getFieldType(Field field) {
		return AnnoAndRefleCache.getAnnoAndRefleCache().getRAMCacheFieldType(field);
	}

	/**
	 * 
	 * @param entity
	 * @param fields
	 * @param whereClause
	 * @param whereArgs
	 */
	public static <E> String getWhereSQL(final E entity, final Field[] fields, final String[] whereArgs) {
		
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < fields.length; i ++) {
			Field field = fields[i];
			field.setAccessible(true);
			String name = getColumnName(field);
			
			if(i == fields.length - 1) {
				sb.append(" " + name + "=? ");
			} else {
				sb.append(" " + name + "=? and ");
			}
			
			try {
				whereArgs[i] = field.get(entity).toString();
			} catch (IllegalAccessException | IllegalArgumentException e) {
				Log.e("ORM", e.toString());
				continue;
			}
		}
		return sb.toString();
	}
}
