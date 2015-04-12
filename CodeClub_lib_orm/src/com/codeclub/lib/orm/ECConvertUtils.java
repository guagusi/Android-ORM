package com.codeclub.lib.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.codeclub.lib.orm.annotation.Column;

/**
 * 
 * @Description: Entity ContentValues 转换工具类
 * @author guagusi123@gmail.com
 * @date 2015年4月12日 上午12:57:14
 */
public class ECConvertUtils {

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public static final <E> ContentValues convert2CV(E entity) {
		if (entity == null) {
			return null;
		}

		ContentValues cv = new ContentValues();
		Field[] fields = entity.getClass().getFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				field.setAccessible(true);
				try {
					if(field.get(entity) != null) {
						// 主键自增长情况下，不可以设为0
						if(isPrimaryKey(field) && "0".equalsIgnoreCase(field.get(entity).toString()) && isAutoIncrement(field)) {
							continue;
						} else {
							cv.put(SQLUtils.getColumnName(field), field.get(entity)
									.toString());
						}
						
					}
					
				} catch (IllegalAccessException | IllegalArgumentException e) {
					Log.e("ORM", e.toString());
					continue;
				}
			}
		}

		return cv;
	}

	/**
	 * 
	 * @param entity
	 * @param cursor
	 * @return
	 */
	public static final <E> E convert2Entity(E entity, Cursor cursor) {
		if (cursor == null || !cursor.moveToFirst()) {
			return null;
		}

		Field[] fields = entity.getClass().getFields();
		Class<?> fieldType = null;
		int index = 0;
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			field.setAccessible(true);
			fieldType = field.getType();
			index = cursor.getColumnIndex(SQLUtils.getColumnName(field));

			try {
				if (fieldType == String.class) {
					field.set(entity, cursor.getString(index));
				} else if (fieldType == Integer.class || fieldType == int.class
						|| fieldType == Long.class || fieldType == long.class
						|| fieldType == short.class || fieldType == Short.class) {
					field.setInt(entity, cursor.getInt(index));
				} else if (fieldType == double.class
						|| fieldType == Double.class) {
					field.setDouble(entity, cursor.getDouble(index));
				} else if (fieldType == float.class || fieldType == Float.class) {
					field.setFloat(entity, cursor.getFloat(index));
				} else if (fieldType == byte[].class
						|| fieldType == Byte[].class) {
					// field.set(entity, cursor.getBlob(index));
				}
			} catch (Exception e) {
				Log.e("ORM", e.toString());
				continue;
			}
		}

		return entity;
	}

	/**
	 * 
	 * @param clazz
	 * @param cursor
	 * @return
	 */
	public static final <E> List<E> convert2EntityList(Class<E> clazz, Cursor cursor) {
		if (cursor == null || !cursor.moveToFirst()) {
			return null;
		}

		
		Class<?> fieldType = null;
		List<E> entityList = new ArrayList<E>();

		try {
			do {
				int index = 0;
				E entity = clazz.newInstance();
				Field[] fields = entity.getClass().getFields();

				for (Field field : fields) {
					if (!field.isAnnotationPresent(Column.class)) {
						continue;
					}
					field.setAccessible(true);
					fieldType = field.getType();
					index = cursor
							.getColumnIndex(SQLUtils.getColumnName(field));

					if (fieldType == String.class) {
						field.set(entity, cursor.getString(index));
					} else if (fieldType == Integer.class
							|| fieldType == int.class
							|| fieldType == Long.class
							|| fieldType == long.class
							|| fieldType == short.class
							|| fieldType == Short.class) {
						field.setInt(entity, cursor.getInt(index));
					} else if (fieldType == double.class
							|| fieldType == Double.class) {
						field.setDouble(entity, cursor.getDouble(index));
					} else if (fieldType == float.class
							|| fieldType == Float.class) {
						field.setFloat(entity, cursor.getFloat(index));
					} else if (fieldType == byte[].class
							|| fieldType == Byte[].class) {
						// field.set(entity, cursor.getBlob(index));
					}
				}

				entityList.add(entity);
			} while (cursor.moveToNext());
		} catch (Exception e) {
			Log.e("ORM", e.toString());
		}

		return entityList;
	}

	/**
	 * 是否主键
	 * @param field
	 * @return
	 */
	private static boolean isPrimaryKey(Field field) {
		if(field != null && field.isAnnotationPresent(Column.class)) {
			return field.getAnnotation(Column.class).primaryKey();
		}
		return false;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	private static boolean isAutoIncrement(Field field) {
		if(field != null && field.isAnnotationPresent(Column.class)) {
			return field.getAnnotation(Column.class).autoIncrease();
		}
		return false;
	}
	
}
